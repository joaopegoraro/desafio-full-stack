package br.edu.unoesc.desafiofullstack.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.stereotype.Service;

import br.edu.unoesc.desafiofullstack.exceptions.BadCredentialsException;
import br.edu.unoesc.desafiofullstack.exceptions.InternalException;
import br.edu.unoesc.desafiofullstack.exceptions.UserAlreadyRegisteredException;
import br.edu.unoesc.desafiofullstack.models.User;
import br.edu.unoesc.desafiofullstack.repositories.UserRepository;
import br.edu.unoesc.desafiofullstack.utils.PasswordHasher;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserAuthenticated(HttpSession session) {
        try {
            return session.getAttribute("username") != null;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public void authenticateSession(HttpSession session, String username) throws InternalException {
        try {
            session.setAttribute("username", username);
        } catch (IllegalStateException e) {
            throw new InternalException(e);
        }
    }

    public void performLogin(String username, String password) throws BadCredentialsException, InternalException {
        final User user = userRepository
                .findById(username)
                .orElseThrow(() -> new BadCredentialsException());

        try {
            final boolean isPasswordValid = PasswordHasher.validatePassword(password, user.getHash());
            if (!isPasswordValid) {
                throw new BadCredentialsException();
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalException(e);
        }
    }

    public void createAccount(String username, String password)
            throws InternalException, UserAlreadyRegisteredException {
        if (userRepository.existsById(username)) {
            throw new UserAlreadyRegisteredException();
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setHash(PasswordHasher.hashPassword(password));
            user = userRepository.save(user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalException(e);
        }
    }
}

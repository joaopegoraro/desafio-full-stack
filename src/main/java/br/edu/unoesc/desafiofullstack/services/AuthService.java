package br.edu.unoesc.desafiofullstack.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.stereotype.Service;

import br.edu.unoesc.desafiofullstack.exceptions.BadCredentialsException;
import br.edu.unoesc.desafiofullstack.exceptions.InternalException;
import br.edu.unoesc.desafiofullstack.models.Hash;
import br.edu.unoesc.desafiofullstack.models.User;
import br.edu.unoesc.desafiofullstack.repositories.HashRepository;
import br.edu.unoesc.desafiofullstack.repositories.UserRepository;
import br.edu.unoesc.desafiofullstack.utils.PasswordHasher;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final HashRepository hashRepository;

    public AuthService(UserRepository userRepository, HashRepository hashRepository) {
        this.userRepository = userRepository;
        this.hashRepository = hashRepository;
    }

    public boolean isUserAuthenticated(HttpSession session) {
        try {
            return session.getAttribute("username") != null;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public void authenticateSession(HttpSession session, String username) {
        session.setAttribute("username", username);
    }

    public void performLogin(String username, String password) throws BadCredentialsException, InternalException {
        final Hash hash = hashRepository
                .findById(username)
                .orElseThrow(() -> new BadCredentialsException());

        if (userRepository.findById(username).isEmpty()) {
            hashRepository.deleteById(username);
            throw new BadCredentialsException();
        }

        try {
            final boolean isPasswordValid = PasswordHasher.validatePassword(password, hash.getHash());
            if (!isPasswordValid) {
                throw new BadCredentialsException();
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalException(e);
        }
    }

    public void createAccount(String username, String password) throws InternalException {
        User user = new User(username);
        user = userRepository.save(user);
        final Hash hash = new Hash();
        hash.setUsername(user.getUsername());
        try {
            hash.setHash(PasswordHasher.hashPassword(user.getUsername()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalException(e);
        }
        hashRepository.save(hash);
    }
}

package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.edu.unoesc.desafiofullstack.dtos.LoginDto;
import br.edu.unoesc.desafiofullstack.dtos.RegisterDto;
import br.edu.unoesc.desafiofullstack.exceptions.BadCredentialsException;
import br.edu.unoesc.desafiofullstack.exceptions.InternalException;
import br.edu.unoesc.desafiofullstack.exceptions.UserAlreadyRegisteredException;
import br.edu.unoesc.desafiofullstack.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String getRegister(RegisterDto registerDto) {
        return "auth/register";
    }

    @RequestMapping(path = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(LoginDto loginDto, HttpServletRequest request, Model model) {
        try {
            final String username = loginDto.getUsername();
            final String password = loginDto.getPassword();
            authService.performLogin(username, password);

            final HttpSession session = request.getSession();
            authService.authenticateSession(session, username);

            return "redirect:/";
        } catch (BadCredentialsException e) {
            model.addAttribute("loginError", true);
            return "auth/login";
        } catch (InternalException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/cadastro")
    public String postRegister(RegisterDto registerDto, HttpServletRequest request, Model model) {
        if (!registerDto.isPasswordValid()) {
            model.addAttribute("errorMessage", "A senha deve conter entre 6 e 40 caracteres");
            registerDto.clearPasswords();
            return "auth/register";
        }

        if (!registerDto.arePasswordsEqual()) {
            model.addAttribute("errorMessage", "As senhas devem ser iguais");
            registerDto.clearPasswords();
            return "auth/register";
        }

        try {
            final String username = registerDto.getUsername();
            final String password = registerDto.getPassword();
            authService.createAccount(username, password);

            final HttpSession session = request.getSession();
            authService.authenticateSession(session, username);

            return "redirect:/";
        } catch (UserAlreadyRegisteredException e) {
            model.addAttribute("errorMessage", "Já existe um usuário cadastrado com esse nome de usuário");
            registerDto.clearPasswords();
            return "auth/register";
        } catch (InternalException e) {
            return "redirect:/error";
        }
    }
}

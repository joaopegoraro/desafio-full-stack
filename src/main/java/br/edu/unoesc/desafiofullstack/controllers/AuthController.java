package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.unoesc.desafiofullstack.dtos.LoginDto;
import br.edu.unoesc.desafiofullstack.exceptions.BadCredentialsException;
import br.edu.unoesc.desafiofullstack.exceptions.InternalException;
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
    public String getLogin(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (authService.isUserAuthenticated(session)) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @PostMapping(path = "/login")
    public String postLogin(
            LoginDto loginDto,
            HttpServletRequest request,
            Model model) {
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

    @GetMapping("/cadastrar")
    public String getRegister(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (authService.isUserAuthenticated(session)) {
            return "redirect:/";
        }
        return "auth/register";
    }
}

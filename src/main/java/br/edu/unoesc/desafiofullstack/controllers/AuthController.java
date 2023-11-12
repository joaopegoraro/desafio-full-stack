package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.edu.unoesc.desafiofullstack.dtos.LoginDto;
import br.edu.unoesc.desafiofullstack.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(path = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(
            HttpServletRequest request,
            @RequestBody LoginDto loginDto) {
        final HttpSession session = request.getSession();

        if (authService.isUserAuthenticated(session)) {
            return "redirect:/";
        }

        return "auth/login";
    }
}

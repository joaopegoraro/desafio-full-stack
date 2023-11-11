package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String greeting() {
        return "auth/login";
    }
}

package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {

    @GetMapping({ "/", "pessoas" })
    public String getPersons() {
        return "persons/list";
    }

}

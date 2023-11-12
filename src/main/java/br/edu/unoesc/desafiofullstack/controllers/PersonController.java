package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.unoesc.desafiofullstack.models.Person;

@Controller
public class PersonController {

    @GetMapping({ "/", "/pessoas" })
    public String getPersons() {
        return "persons/list";
    }

    @GetMapping("/pessoas/cadastro")
    public String getRegister(Person person, Model model) {
        return "persons/register";
    }

}

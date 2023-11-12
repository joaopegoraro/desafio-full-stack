package br.edu.unoesc.desafiofullstack.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.unoesc.desafiofullstack.models.Person;
import br.edu.unoesc.desafiofullstack.repositories.PersonRepository;

@Controller
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping({ "/", "/pessoas" })
    public String getPersons(Model model) {
        final Iterable<Person> list = personRepository.findAll();
        model.addAttribute("list", list);
        return "persons/list";
    }

    @GetMapping("/pessoas/cadastro")
    public String getRegister(Person person) {
        return "persons/register";
    }

    @GetMapping("/pessoas/{id}/editar")
    public String getEdit(Person person, Model model) {
        model.addAttribute("editMode", true);
        return "persons/register";
    }

    @PostMapping("/pessoas/cadastro")
    public String postRegister(Person person, Model model) {
        // mantém apenas os números do CPF
        person.setCpf(person.getCpf().replaceAll("\\D+", ""));

        try {
            personRepository.save(person);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Já existe uma pessoa cadastrada com esse CPF");
            return "persons/register";
        }
    }

    @PostMapping("/pessoas/{id}/editar")
    public String postEdit(Person person, Model model) {
        model.addAttribute("editMode", true);

        // mantém apenas os números do CPF
        person.setCpf(person.getCpf().replaceAll("\\D+", ""));

        try {
            personRepository.save(person);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Já existe uma pessoa cadastrada com esse CPF");
            return "persons/register";
        }
    }

}

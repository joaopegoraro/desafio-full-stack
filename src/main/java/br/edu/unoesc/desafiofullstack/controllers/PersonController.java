package br.edu.unoesc.desafiofullstack.controllers;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.unoesc.desafiofullstack.models.Person;
import br.edu.unoesc.desafiofullstack.models.PersonFilter;
import br.edu.unoesc.desafiofullstack.repositories.PersonRepository;

@Controller
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping({ "/", "/pessoas" })
    public String getPersons(PersonFilter filter, Model model) {
        final List<Person> list;
        if (filter.isEmpty()) {
            list = personRepository.findAll();
        } else {
            final Person example = new Person();
            ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues()
                    .withIgnorePaths("contacts", "addresses", "id");

            if (filter.getId() != null) {
                System.err.println(filter.getId());
                exampleMatcher = exampleMatcher.withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
                example.setId(filter.getId());
            }

            if (filter.getName() != null && !filter.getName().isBlank()) {
                exampleMatcher = exampleMatcher.withMatcher("name",
                        ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
                example.setName(filter.getName());
            }

            if (filter.getCpf() != null && !filter.getCpf().isBlank()) {
                exampleMatcher = exampleMatcher.withMatcher("cpf",
                        ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
                // mantém apenas os números do CPF
                example.setCpf(filter.getCpf().replaceAll("\\D+", ""));
            }

            if (filter.getBirthdate() != null && !filter.getBirthdate().isBlank()) {
                example.setBirthdate(Date.valueOf(filter.getBirthdate()));
            }

            example.setGender(filter.getGender());

            list = personRepository.findAll(Example.of(example, exampleMatcher));
        }
        model.addAttribute("list", list);
        model.addAttribute("filter", filter);
        return "persons/list";
    }

    @GetMapping("/pessoas/cadastro")
    public String getRegister(Person person) {
        return "persons/register";
    }

    @GetMapping("/pessoas/{id}")
    public String getDetail(@PathVariable("id") Long id, Model model) {
        try {
            final Person person = personRepository.findById(id).get();
            model.addAttribute("person", person);
            return "persons/detail";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/pessoas/{id}/editar")
    public String getEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("editMode", true);
        try {
            final Person person = personRepository.findById(id).get();
            model.addAttribute("person", person);
            return "persons/register";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
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
    public String postEdit(@PathVariable("id") Long id, Person person, Model model) {
        model.addAttribute("editMode", true);

        try {
            person.setId(id);

            // mantém apenas os números do CPF
            person.setCpf(person.getCpf().replaceAll("\\D+", ""));

            personRepository.save(person);

            return "redirect:/pessoas/" + id;
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Já existe uma pessoa cadastrada com esse CPF");
            return "persons/register";
        }
    }

    @PostMapping("/pessoas/{id}/deletar")
    public String postDelete(@PathVariable("id") Long id) {
        personRepository.deleteById(id);
        return "redirect:/pessoas";
    }

}

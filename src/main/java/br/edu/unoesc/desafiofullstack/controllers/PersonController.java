package br.edu.unoesc.desafiofullstack.controllers;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getPersons(@RequestParam("page") Optional<Integer> page, PersonFilter filter, Model model) {
        final Page<Person> list;
        final int currentPage = page.orElse(1);

        if (filter.isEmpty()) {
            final Order order = Order.asc("id");
            final Sort sort = Sort.by(order);
            list = personRepository.findAll(PageRequest.of(currentPage - 1, 10, sort));
        } else {
            final Person example = new Person();
            ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues()
                    .withIgnorePaths("contacts", "addresses");

            if (filter.getId() != null) {
                System.err.println(filter.getId());
                exampleMatcher = exampleMatcher.withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact());
                example.setId(filter.getId());
            } else {
                exampleMatcher = exampleMatcher.withIgnorePaths("id");
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

            if (filter.getGender() != null && !filter.getGender().isBlank()) {
                example.setGender(filter.getGender());
            }

            final Order order = filter.getOrderBy().equals("asc")
                    ? Order.asc(filter.getSortBy())
                    : Order.desc(filter.getSortBy());
            final Pageable pagination = PageRequest.of(currentPage - 1, filter.getSize(), Sort.by(order));
            list = personRepository.findAll(Example.of(example, exampleMatcher), pagination);
        }

        model.addAttribute("list", list);
        model.addAttribute("filter", filter);
        model.addAttribute("queryString", filter.asQueryString());

        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            final int rangeStart = Math.min(1, currentPage);
            final int rangeEnd = Math.max(currentPage, totalPages);
            final List<Integer> pageNumbers = IntStream.rangeClosed(rangeStart, rangeEnd)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("page", currentPage);
        }

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

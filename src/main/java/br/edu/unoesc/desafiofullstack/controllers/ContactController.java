package br.edu.unoesc.desafiofullstack.controllers;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.unoesc.desafiofullstack.models.Contact;
import br.edu.unoesc.desafiofullstack.models.Person;
import br.edu.unoesc.desafiofullstack.repositories.ContactRepository;
import br.edu.unoesc.desafiofullstack.repositories.PersonRepository;

@Controller
public class ContactController {

    private final ContactRepository contactRepository;
    private final PersonRepository personRepository;

    public ContactController(ContactRepository contactRepository, PersonRepository personRepository) {
        this.contactRepository = contactRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/pessoas/{personId}/contatos/cadastro")
    public String getRegister(@PathVariable("personId") Long personId, Contact contact, Model model) {
        try {
            final Person person = personRepository.findById(personId).get();
            model.addAttribute("person", person);
            return "persons/register_contact";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/pessoas/{personId}/contatos/{id}/editar")
    public String getEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("editMode", true);
        try {
            final Contact contact = contactRepository.findById(id).get();

            model.addAttribute("contact", contact);
            model.addAttribute("person", contact.getPerson());

            return "persons/register_contact";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/pessoas/{personId}/contatos/cadastro")
    public String postRegister(@PathVariable("personId") Long personId, Contact contact, Model model) {
        try {
            final Person person = personRepository.findById(personId).get();
            contact.setPerson(person);
            model.addAttribute("person", contact.getPerson());

            // mantém apenas os números do telefone
            contact.setPhone(contact.getPhone().replaceAll("\\D+", ""));

            if (contactRepository.existsWithPhoneAndPerson(contact.getPhone(), personId)) {
                model.addAttribute("errorMessage", "Já existe um contato cadastrado com esse telefone");
                return "persons/register_contact";
            } else if (contactRepository.existsWithEmailAndPerson(contact.getEmail(), personId)) {
                model.addAttribute("errorMessage", "Já existe um contato cadastrado com esse e-mail");
                return "persons/register_contact";
            }

            contactRepository.save(contact);

            return "redirect:/pessoas/" + personId;
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/pessoas/{personId}/contatos/{id}/editar")
    public String postEdit(
            @PathVariable("personId") Long personId,
            @PathVariable("id") Long id,
            Contact contact,
            Model model) {

        model.addAttribute("editMode", true);

        contact.setId(id);

        final Person person = personRepository.findById(personId).get();
        contact.setPerson(person);
        model.addAttribute("person", contact.getPerson());

        // mantém apenas os números do telefone
        contact.setPhone(contact.getPhone().replaceAll("\\D+", ""));

        if (contactRepository.existsWithPhoneAndPersonAndDifferentId(contact.getPhone(), personId, id)) {
            model.addAttribute("errorMessage", "Já existe um contato cadastrado com esse telefone");
            return "persons/register_contact";
        } else if (contactRepository.existsWithEmailAndPersonAndDifferentId(contact.getEmail(), personId, id)) {
            model.addAttribute("errorMessage", "Já existe um contato cadastrado com esse e-mail");
            return "persons/register_contact";
        }

        contactRepository.save(contact);

        return "redirect:/pessoas/" + personId;
    }

    @PostMapping("/pessoas/{personId}/contatos/{id}/deletar")
    public String postDelete(@PathVariable("personId") Long personId, @PathVariable("id") Long id) {
        contactRepository.deleteById(id);
        return "redirect:/pessoas/" + personId;
    }

}

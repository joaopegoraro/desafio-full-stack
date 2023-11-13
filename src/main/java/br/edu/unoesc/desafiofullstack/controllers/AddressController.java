package br.edu.unoesc.desafiofullstack.controllers;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.unoesc.desafiofullstack.models.Address;
import br.edu.unoesc.desafiofullstack.models.Person;
import br.edu.unoesc.desafiofullstack.repositories.AddressRepository;
import br.edu.unoesc.desafiofullstack.repositories.PersonRepository;

@Controller
public class AddressController {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;

    public AddressController(AddressRepository addressRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/pessoas/{personId}/enderecos/cadastro")
    public String getRegister(@PathVariable("personId") Long personId, Address address, Model model) {
        try {
            final Person person = personRepository.findById(personId).get();
            model.addAttribute("person", person);
            return "persons/register_address";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/pessoas/{personId}/enderecos/{id}/editar")
    public String getEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("editMode", true);
        try {
            final Address address = addressRepository.findById(id).get();

            model.addAttribute("address", address);
            model.addAttribute("person", address.getPerson());

            return "persons/register_address";
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/pessoas/{personId}/enderecos/cadastro")
    public String postRegister(@PathVariable("personId") Long personId, Address address, Model model) {
        try {
            final Person person = personRepository.findById(personId).get();
            address.setPerson(person);
            model.addAttribute("person", address.getPerson());

            // mantém apenas os números do cep
            address.setCep(address.getCep().replaceAll("\\D+", ""));

            addressRepository.save(address);

            return "redirect:/pessoas/" + personId;
        } catch (NoSuchElementException e) {
            return "redirect:/error";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Já existe um endereço cadastrado com esse CEP");
            return "persons/register_address";
        }
    }

    @PostMapping("/pessoas/{personId}/enderecos/{id}/editar")
    public String postEdit(
            @PathVariable("personId") Long personId,
            @PathVariable("id") Long id,
            Address address,
            Model model) {

        model.addAttribute("editMode", true);
        try {
            address.setId(id);

            final Person person = personRepository.findById(personId).get();
            address.setPerson(person);
            model.addAttribute("person", address.getPerson());

            // mantém apenas os números do cep
            address.setCep(address.getCep().replaceAll("\\D+", ""));

            addressRepository.save(address);

            return "redirect:/pessoas/" + personId;
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Já existe um endereço cadastrado com esse CEP");
            return "persons/register_address";
        }
    }

    @PostMapping("/pessoas/{personId}/enderecos/{id}/deletar")
    public String postDelete(@PathVariable("personId") Long personId, @PathVariable("id") Long id) {
        addressRepository.deleteById(id);
        return "redirect:/pessoas/" + personId;
    }

}

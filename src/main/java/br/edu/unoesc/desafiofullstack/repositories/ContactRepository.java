package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.unoesc.desafiofullstack.models.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {
}

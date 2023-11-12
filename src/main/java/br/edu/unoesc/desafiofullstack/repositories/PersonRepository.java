package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.unoesc.desafiofullstack.models.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
}

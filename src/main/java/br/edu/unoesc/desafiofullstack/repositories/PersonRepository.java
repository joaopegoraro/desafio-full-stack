package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unoesc.desafiofullstack.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

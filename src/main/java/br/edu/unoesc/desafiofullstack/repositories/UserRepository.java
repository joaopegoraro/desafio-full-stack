package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.unoesc.desafiofullstack.models.User;

public interface UserRepository extends CrudRepository<User, String> {
}

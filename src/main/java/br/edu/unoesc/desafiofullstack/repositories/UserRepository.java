package br.edu.unoesc.desafiofullstack.repositories;

import br.edu.unoesc.desafiofullstack.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}

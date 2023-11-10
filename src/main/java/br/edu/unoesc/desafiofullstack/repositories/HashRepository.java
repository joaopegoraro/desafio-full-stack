package br.edu.unoesc.desafiofullstack.repositories;

import br.edu.unoesc.desafiofullstack.models.Hash;
import org.springframework.data.repository.CrudRepository;

public interface HashRepository extends CrudRepository<Hash, String> {
}

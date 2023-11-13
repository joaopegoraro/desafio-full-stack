package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.repository.CrudRepository;

import br.edu.unoesc.desafiofullstack.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}

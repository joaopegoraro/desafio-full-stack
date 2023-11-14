package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.edu.unoesc.desafiofullstack.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("SELECT CASE WHEN COUNT(a)> 0 THEN TRUE ELSE FALSE END FROM Address a WHERE a.person.id = :personId AND a.cep = :cep")
    public boolean existsWithCepAndPerson(
            @Param("cep") String cep,
            @Param("personId") Long personId);

    @Query("SELECT CASE WHEN COUNT(a)> 0 THEN TRUE ELSE FALSE END FROM Address a WHERE a.id != :id AND a.person.id = :personId AND a.cep = :cep")
    public boolean existsWithCepAndPersonAndDifferentId(
            @Param("cep") String cep,
            @Param("personId") Long personId,
            @Param("id") Long id);
}

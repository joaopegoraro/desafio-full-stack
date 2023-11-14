package br.edu.unoesc.desafiofullstack.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.edu.unoesc.desafiofullstack.models.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {

        @Query("SELECT CASE WHEN COUNT(c)> 0 THEN TRUE ELSE FALSE END FROM Contact c WHERE c.person.id = :personId AND c.phone = :phone")
        public boolean existsWithPhoneAndPerson(
                        @Param("phone") String phone,
                        @Param("personId") Long personId);

        @Query("SELECT CASE WHEN COUNT(c)> 0 THEN TRUE ELSE FALSE END FROM Contact c WHERE c.email != '' AND c.person.id = :personId AND c.email = :email")
        public boolean existsWithEmailAndPerson(
                        @Param("email") String email,
                        @Param("personId") Long personId);

        @Query("SELECT CASE WHEN COUNT(c)> 0 THEN TRUE ELSE FALSE END FROM Contact c WHERE c.id != :id AND c.person.id = :personId AND c.phone = :phone")
        public boolean existsWithPhoneAndPersonAndDifferentId(
                        @Param("phone") String phone,
                        @Param("personId") Long personId,
                        @Param("id") Long id);

        @Query("SELECT CASE WHEN COUNT(c)> 0 THEN TRUE ELSE FALSE END FROM Contact c WHERE c.id != :id AND c.email != '' AND c.person.id = :personId AND c.email = :email")
        public boolean existsWithEmailAndPersonAndDifferentId(
                        @Param("email") String email,
                        @Param("personId") Long personId,
                        @Param("id") Long id);
}

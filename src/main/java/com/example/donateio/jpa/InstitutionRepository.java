package com.example.donateio.jpa;

import com.example.donateio.model.Institution;
import com.example.donateio.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InstitutionRepository extends CrudRepository<Institution, Long> {

    Optional<Institution> findByEmail(String email);


}

package com.pierina.psiweb.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Profissional;

@Repository
public interface ProfissionalRepository extends CrudRepository<Profissional, Integer>{
	Optional<Profissional> findByEmail(String email);
}

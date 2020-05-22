package com.pierina.psiweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Profissional;

@Repository
public interface ProfissionalRepository extends CrudRepository<Profissional, Integer>{

}

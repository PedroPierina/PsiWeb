package com.pierina.psiweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Paciente;

@Repository
public interface PacienteRepository extends CrudRepository<Paciente, Integer>{

}

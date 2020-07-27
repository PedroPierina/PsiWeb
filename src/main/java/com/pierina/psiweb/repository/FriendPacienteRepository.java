package com.pierina.psiweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pierina.psiweb.modal.FriendPaciente;
import com.pierina.psiweb.modal.Paciente;

public interface FriendPacienteRepository extends CrudRepository<FriendPaciente, Integer>{
	List<FriendPaciente> findAllByPaciente(Optional<Paciente> userFound);
}

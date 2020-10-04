package com.pierina.psiweb.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Paciente;
import com.pierina.psiweb.modal.Profissional;

@Repository
public interface PacienteRepository extends CrudRepository<Paciente, Integer>{
	Optional<Paciente> findByEmail(String email); 
	
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Paciente SET profile_picture = :base64 WHERE email = :email")
    int updatePicture(@Param("email") String email, @Param("base64") String base64);

	@Query("SELECT m FROM Paciente m WHERE m.name like %:name% and m.lastName like %:lastName% ")
	List<Paciente> search(@Param("name") String name, @Param("lastName") String lastName); 
	
	@Query("SELECT m FROM Paciente m WHERE m.name like %:name%")
	List<Paciente> searchSimple(@Param("name") String name); 
	
	@Transactional 
	void deleteByEmail(String email); 
}

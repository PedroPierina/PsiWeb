package com.pierina.psiweb.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Profissional;

@Repository
public interface ProfissionalRepository extends CrudRepository<Profissional, Integer>{
	Optional<Profissional> findByEmail(String email);
	
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Profissional SET profile_picture = :base64 WHERE email = :email")
    int updatePicture(@Param("email") String email, @Param("base64") String base64);
	
	@Query("SELECT m FROM Profissional m WHERE m.name like %:name% and m.lastName like %:lastName% ")
	List<Profissional> search(@Param("name") String name, @Param("lastName") String lastName); 
	
	@Query("SELECT m FROM Profissional m WHERE m.name like %:name%")
	List<Profissional> searchSimple(@Param("name") String name); 
	
	
	
}

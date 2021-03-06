package com.pierina.psiweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Paciente;
import com.pierina.psiweb.modal.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer>{
	Optional<Post> findByUserEmail(String userEmail);

	List<Post> findAllByUserEmail(String userEmail);
}

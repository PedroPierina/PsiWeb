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
import com.pierina.psiweb.modal.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer>{
	Optional<Post> findByUserEmail(String userEmail);

	List<Post> findAllByUserEmail(String userEmail);
	
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Post SET likes = :likes WHERE post_id = :id")
    int plusLike(@Param("likes") int likes, @Param("id") int id);
}

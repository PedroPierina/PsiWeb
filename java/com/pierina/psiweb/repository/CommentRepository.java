package com.pierina.psiweb.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pierina.psiweb.modal.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer>{
	Optional<Comment> findByUserEmail(String userEmail);
}

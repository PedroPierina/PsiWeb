package com.pierina.psiweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pierina.psiweb.modal.Comment;
import com.pierina.psiweb.modal.Message;

public interface MessageRepository extends CrudRepository<Message, Integer>{
	@Query("SELECT m FROM Message m WHERE m.fromEmail = :fromEmail AND m.toEmail = :toEmail")
	List<Message> findByfromEmailtoEmail(String fromEmail, String toEmail);
	
	List<Message> findByfromEmail(String fromEmail);
	
	List<Message> findByToEmail(String toEmail);
}

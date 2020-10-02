package com.pierina.psiweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pierina.psiweb.modal.Friend;
import com.pierina.psiweb.modal.Profissional;

public interface FriendRepository extends CrudRepository<Friend, Integer>{
	List<Friend> findAllByProfissional(Optional<Profissional> userFound);
}

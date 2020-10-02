package com.pierina.psiweb.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class FriendPaciente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String name;
	
	@Lob
	private String profilePicture;
	private int type;
	
	private int FriendId;
	
	@ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "paciente_id", nullable = false)
    private Paciente paciente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFriendId() {
		return FriendId;
	}

	public void setFriendId(int friendId) {
		FriendId = friendId;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	
	
}

package com.pierina.psiweb.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

@Entity
public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String name;
	
	@Lob
	private String profilePicture;
	private int type;
	
	private int friendId;
	
	@ManyToOne
    @JoinColumn(name = "profissional_id", referencedColumnName = "profissional_id", nullable = false)
    private Profissional profissional;
	
	
	
	public String getName() {
		return name;
	}

	public int getFriendId() {
		return friendId;
	}
	
	public void setFriendId(int friendId) {
		this.friendId = friendId;
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
	public void setType(int type2) {
		this.type = type2;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Profissional getProfissional() {
		return profissional;
	}
	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	
}

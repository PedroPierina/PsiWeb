package com.pierina.psiweb.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class user {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@NotEmpty(message = "Nome do ususario eh um campo obrigatorio")
	private String userName;
	private String password;
	@NotEmpty(message = "Senha eh um campo obrigatorio")
	private String passwordHash;
	@NotEmpty(message = "Tipo do ususario eh um campo obrigatorio")
	private String type;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}

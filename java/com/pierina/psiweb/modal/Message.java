package com.pierina.psiweb.modal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="message")
public class Message implements Comparable<Message>{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String body;
	
	@Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;
	
	private String fromEmail;
	private String fromName;
	private int fromType;
	private String toEmail;
	private int toType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public int getFromType() {
		return fromType;
	}
	public void setFromType(int fromType) {
		this.fromType = fromType;
	}
	public int getToType() {
		return toType;
	}
	public void setToType(int toType) {
		this.toType = toType;
	}
	
	public int compareTo(Message o) {
	    return getCreateDate().compareTo(o.getCreateDate());
	  }
	
}

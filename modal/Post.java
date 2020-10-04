package com.pierina.psiweb.modal;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "post_id")
	private int id;
	private String title;
	
	@Lob
	private String body;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date creatDate;
	
	private String userEmail;
	private String name;
	private int likes;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

	
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", body=" + body + ", creatDate=" + creatDate + ", userEmail="
				+ userEmail + ", comments=" + comments + "]";
	}
	
	
}

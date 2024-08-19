package com.apk.login.modelo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)  // Cambia a LAZY si prefieres cargar la entidad Business solo cuando se necesite
    @JoinColumn(name = "business_id", nullable = false)  // Usamos @JoinColumn en lugar de @Column
    private Business business;

    @ManyToOne(fetch = FetchType.EAGER)  // Cambia a LAZY si prefieres cargar la entidad Business solo cuando se necesite
    @JoinColumn(name = "user_id", nullable = false)  // Usamos @JoinColumn en lugar de @Column
    private User user;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)   
    //@JsonManagedReference
    private List<CommentResponse> responses = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	@JsonIgnore
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<CommentResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<CommentResponse> responses) {
		this.responses = responses;
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
}

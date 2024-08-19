package com.apk.login.modelo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comment_responses")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CommentResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    private String id;

    @Column(name = "comment_id", nullable = false)  // Usamos @JoinColumn en lugar de @Column
    private String comment;

    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Usamos @JoinColumn en lugar de @Column
    private User user;


    @Column(name = "response", nullable = false)
    private String respuesta;

    @Temporal(TemporalType.DATE)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public CommentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

   
    
    
}

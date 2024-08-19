package com.apk.login.dto;

import java.util.Date;
import java.util.List;

import com.apk.login.modelo.User;

public class CommentDTO {
    private String id;
    private User user;
    private String comment;
    private int rating;
    private Date timestamp;
    private List<CommentResponseDTO> responses;
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
	public List<CommentResponseDTO> getResponses() {
		return responses;
	}
	public void setResponses(List<CommentResponseDTO> responses) {
		this.responses = responses;
	}
	public CommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

    
}

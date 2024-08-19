package com.apk.login.dto;

import java.util.Date;

import com.apk.login.modelo.Comment;
import com.apk.login.modelo.User;

public class CommentResponseDTO {

	private User user;
	private String commentId;
    private String response;
    private Date timestamp;

	
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
    
    
}

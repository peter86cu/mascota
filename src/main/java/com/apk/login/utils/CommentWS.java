package com.apk.login.utils;

import com.apk.login.modelo.CommentResponse;
import com.apk.login.modelo.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentWS implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String actividadid;

	private User user;

	private String comment;

	private int rating;

	private Date timestamp;

	private List<CommentResponse> responses = new ArrayList<>();

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

	public List<CommentResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<CommentResponse> responses) {
		this.responses = responses;
	}

	public CommentWS() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getActividadid() {
		return actividadid;
	}

	public void setActividadid(String actividadid) {
		this.actividadid = actividadid;
	}

}

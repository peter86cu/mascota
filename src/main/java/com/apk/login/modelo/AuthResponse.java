package com.apk.login.modelo;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class AuthResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private User user;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AuthResponse(User user, String token) {
		super();
		this.user = user;
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AuthResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}

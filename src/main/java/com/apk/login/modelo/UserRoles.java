package com.apk.login.modelo;

import java.io.Serializable;


import javax.persistence.*;



@Entity
@Table(name = "user_roles")
public class UserRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	private int rolid;
	private String userid;

	
	
	public UserRoles() {
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRolid() {
		return rolid;
	}

	public void setRolid(int rolid) {
		this.rolid = rolid;
	}

	
	
}
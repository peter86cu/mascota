package com.apk.login.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the calendario_work database table.
 * 
 */
@Entity
@Table(name="calendario_work")
@NamedQuery(name="CalendarioWork.findAll", query="SELECT c FROM CalendarioWork c")
public class CalendarioWork implements Serializable {
	private static final long serialVersionUID = 1L;
	private String endtime;
	@Id
	private int id;
	private String starttime;
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userid") 
	private User userid;

	public CalendarioWork() {
	}


	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getStarttime() {
		return this.starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public User getUserid() {
		return userid;
	}


	public void setUserid(User userid) {
		this.userid = userid;
	}



}
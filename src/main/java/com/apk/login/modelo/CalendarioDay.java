package com.apk.login.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * The persistent class for the calendario_day database table.
 * 
 */
@Entity
@Table(name="calendario_day")
@NamedQuery(name="CalendarioDay.findAll", query="SELECT c FROM CalendarioDay c")
public class CalendarioDay implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String dayid;
	
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "calendarioid") 
	private CalendarioWork calendario;
    @Column(name = "`check`")
	private boolean check;
    @Column(name = "`day`")
	private String day;
	

	public CalendarioDay() {
	}

	public boolean getCheck() {
		return this.check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}


	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}


	public String getDayid() {
		return this.dayid;
	}

	public void setDayid(String dayid) {
		this.dayid = dayid;
	}

	public CalendarioWork getCalendario() {
		return calendario;
	}

	public void setCalendario(CalendarioWork calendario) {
		this.calendario = calendario;
	}




}
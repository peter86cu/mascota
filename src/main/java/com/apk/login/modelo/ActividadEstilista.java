package com.apk.login.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the actividad_estilista database table.
 * 
 */
@Entity
@Table(name="actividad_estilista")
public class ActividadEstilista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id	
	private String actividadid;
	private String description;
	
    @Temporal(TemporalType.DATE)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fecha;
	
    @ManyToOne
    @JoinColumn(name = "mascotaid") 
	private Mascota mascota;
	private String status;
	private String startime;
	private String endtime;
	private String note;
	private double precio;
	private String title;
	private int turnos;
    @ManyToOne
    @JoinColumn(name = "userid") 
    private User usuario;

	public ActividadEstilista() {
	}


	public String getActividadid() {
		return this.actividadid;
	}

	public void setActividadid(String actividadid) {
		this.actividadid = actividadid;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Temporal(TemporalType.DATE)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	
	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	
	public Mascota getMascota() {
		return mascota;
	}


	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}


	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	


	public String getStartime() {
		return startime;
	}


	public void setStartime(String startime) {
		this.startime = startime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public int getTurnos() {
		return turnos;
	}


	public void setTurnos(int turnos) {
		this.turnos = turnos;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}

}
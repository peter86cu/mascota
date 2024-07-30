package com.apk.login.modelo;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the actividad_estilista database table.
 * 
 */
@Entity
@Table(name="actividad_estilista")
@NamedQuery(name="ActividadEstilista.findAll", query="SELECT a FROM ActividadEstilista a")
public class ActividadEstilista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id	
	private int actividadid;
	private String description;
	
    @Temporal(TemporalType.DATE)
	private Date fecha;
	
    @ManyToOne
    @JoinColumn(name = "mascotaid") 
	private Mascota mascota;
	private String status;
	private String startime;
	private String endtime;
	private double precio;
	private String title;
	
    @ManyToOne
    @JoinColumn(name = "userid") 
    private User usuario;

	public ActividadEstilista() {
	}


	public int getActividadid() {
		return this.actividadid;
	}

	public void setActividadid(int actividadid) {
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

}
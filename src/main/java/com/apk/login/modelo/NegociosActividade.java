package com.apk.login.modelo;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the negocios_actividades database table.
 * 
 */
@Entity
@Table(name="businesses_activity")
@NamedQuery(name="NegociosActividade.findAll", query="SELECT n FROM NegociosActividade n")
public class NegociosActividade implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private int actrividad;

	private String descripcion;

	private int id;
	
    @ManyToOne
    @JoinColumn(name = "negocioid") 
	private Business negocio;

	private String precio;

	private int status;

	private String tiempo;

	public NegociosActividade() {
	}

	public int getActrividad() {
		return this.actrividad;
	}

	public void setActrividad(int actrividad) {
		this.actrividad = actrividad;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public Business getNegocio() {
		return negocio;
	}

	public void setNegocio(Business negocio) {
		this.negocio = negocio;
	}

	public String getPrecio() {
		return this.precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

}
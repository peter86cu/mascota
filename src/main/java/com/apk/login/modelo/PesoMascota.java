package com.apk.login.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the tipo_raza database table.
 * 
 */
@Entity
@Table(name="pesos_mascota")
public class PesoMascota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pesoid;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "mascotaid")
    private Mascota mascota;
	
    @Temporal(TemporalType.DATE)
	private Date fecha;

	private double peso;
	
	private String um;

	public PesoMascota() {
	}

	public int getPesoid() {
		return pesoid;
	}

	public void setPesoid(int pesoid) {
		this.pesoid = pesoid;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	
	
}
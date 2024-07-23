package com.apk.login.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the tipo_raza database table.
 * 
 */
@Entity
@Table(name="tipo_vacunas")
public class TipoVacunas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String vacuna;
	
	@Column(name="tiporaza")
	private String tipoRaza;

	public TipoVacunas() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	public String getTipoRaza() {
		return tipoRaza;
	}

	public void setTipoRaza(String tipoRaza) {
		this.tipoRaza = tipoRaza;
	}

	
}
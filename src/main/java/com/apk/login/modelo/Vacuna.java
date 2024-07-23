package com.apk.login.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the vacunas database table.
 * 
 */
@Entity
@Table(name="vacunas")
@NamedQuery(name="Vacuna.findAll", query="SELECT v FROM Vacuna v")
public class Vacuna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int vacunaid;

	@Column(name="clinica_veterinaria")
	private String clinicaVeterinaria;

    @Temporal(TemporalType.DATE)
	@Column(name="fecha_administracion")
	private Date fechaAdministracion;

	@Column(name="lote_vacuna")
	private String loteVacuna;

	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "mascotaid")
    private Mascota mascota;

	@Column(name="nombre_vacuna")
	private String nombreVacuna;

	@Lob
	private String observaciones;

    @Temporal(TemporalType.DATE)
	@Column(name="proxima_fecha_vacunacion")
	private Date proximaFechaVacunacion;

	@Column(name="veterinario_responsable")
	private String veterinarioResponsable;

	public Vacuna() {
	}

	
	public int getVacunaid() {
		return vacunaid;
	}


	public void setVacunaid(int vacunaid) {
		this.vacunaid = vacunaid;
	}


	public String getClinicaVeterinaria() {
		return this.clinicaVeterinaria;
	}

	public void setClinicaVeterinaria(String clinicaVeterinaria) {
		this.clinicaVeterinaria = clinicaVeterinaria;
	}

	public Date getFechaAdministracion() {
		return this.fechaAdministracion;
	}

	public void setFechaAdministracion(Date fechaAdministracion) {
		this.fechaAdministracion = fechaAdministracion;
	}

	public String getLoteVacuna() {
		return this.loteVacuna;
	}

	public void setLoteVacuna(String loteVacuna) {
		this.loteVacuna = loteVacuna;
	}

	

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public String getNombreVacuna() {
		return this.nombreVacuna;
	}

	public void setNombreVacuna(String nombreVacuna) {
		this.nombreVacuna = nombreVacuna;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getProximaFechaVacunacion() {
		return this.proximaFechaVacunacion;
	}

	public void setProximaFechaVacunacion(Date proximaFechaVacunacion) {
		this.proximaFechaVacunacion = proximaFechaVacunacion;
	}

	public String getVeterinarioResponsable() {
		return this.veterinarioResponsable;
	}

	public void setVeterinarioResponsable(String veterinarioResponsable) {
		this.veterinarioResponsable = veterinarioResponsable;
	}

}
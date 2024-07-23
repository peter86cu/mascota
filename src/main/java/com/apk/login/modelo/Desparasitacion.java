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
 * The persistent class for the desparasitaciones database table.
 * 
 */
@Entity
@Table(name = "desparasitaciones")
public class Desparasitacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deparacitacionid;

	@Column(name = "clinica_veterinaria")
	private String clinicaVeterinaria;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_administracion")
	private Date fechaAdministracion;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "mascotaid")
	private Mascota mascota;

	@Column(name = "nombre_producto")
	private String nombreProducto;

	@Lob
	private String observaciones;

	@Temporal(TemporalType.DATE)
	@Column(name = "proxima_fecha_desparasitacion")
	private Date proximaFechaDesparasitacion;

	@Column(name = "tipo_desparasitante")
	private String tipoDesparasitante;

	@Column(name = "veterinario_responsable")
	private String veterinarioResponsable;

	public Desparasitacion() {
	}

	

	public int getDeparacitacionid() {
		return deparacitacionid;
	}



	public void setDeparacitacionid(int deparacitacionid) {
		this.deparacitacionid = deparacitacionid;
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

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public String getNombreProducto() {
		return this.nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getProximaFechaDesparasitacion() {
		return this.proximaFechaDesparasitacion;
	}

	public void setProximaFechaDesparasitacion(Date proximaFechaDesparasitacion) {
		this.proximaFechaDesparasitacion = proximaFechaDesparasitacion;
	}

	public String getTipoDesparasitante() {
		return this.tipoDesparasitante;
	}

	public void setTipoDesparasitante(String tipoDesparasitante) {
		this.tipoDesparasitante = tipoDesparasitante;
	}

	public String getVeterinarioResponsable() {
		return this.veterinarioResponsable;
	}

	public void setVeterinarioResponsable(String veterinarioResponsable) {
		this.veterinarioResponsable = veterinarioResponsable;
	}

}
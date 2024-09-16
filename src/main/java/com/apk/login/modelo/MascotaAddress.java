package com.apk.login.modelo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name="mascota_direccion")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MascotaAddress {
    @Id
    private String id;
	@Column(name = "calle_numero")
    private String calleNumero;
    private String departamento;
    private String localidad;
    private String latitud;
    private String longitud;
    @JsonIgnore
	@ManyToOne
    @JoinColumn(name = "mascotaid")
    private Mascota mascota;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCalleNumero() {
		return calleNumero;
	}
	public void setCalleNumero(String calleNumero) {
		this.calleNumero = calleNumero;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public Mascota getMascota() {
		return mascota;
	}
	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}
	
	
}

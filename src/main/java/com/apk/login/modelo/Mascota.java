package com.apk.login.modelo;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mascota")
public class Mascota implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mascotaid;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private String genero;
    private String color;
    private String tamano;
    private String tipopeso;
    private String personalidad;
    private String historial_medico;
    private String necesidades_especiales;
    private String comportamiento;
    private String fotos;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuarioid") 
    private User usuario;
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacuna> vacunas;

    
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Desparasitacion> desparasitaciones;
    
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PesoMascota> peso;

    // Constructor vacío
    public Mascota() {
    }

	

	public int getMascotaid() {
		return mascotaid;
	}



	public void setMascotaid(int mascotaid) {
		this.mascotaid = mascotaid;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTamañno() {
		return tamano;
	}

	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	

	public String getPersonalidad() {
		return personalidad;
	}

	public void setPersonalidad(String personalidad) {
		this.personalidad = personalidad;
	}

	public String getHistorial_medico() {
		return historial_medico;
	}

	public void setHistorial_medico(String historial_medico) {
		this.historial_medico = historial_medico;
	}

	public String getNecesidades_especiales() {
		return necesidades_especiales;
	}

	public void setNecesidades_especiales(String necesidades_especiales) {
		this.necesidades_especiales = necesidades_especiales;
	}

	public String getComportamiento() {
		return comportamiento;
	}

	public void setComportamiento(String comportamiento) {
		this.comportamiento = comportamiento;
	}

	public String getFotos() {
		return fotos;
	}

	public void setFotos(String fotos) {
		this.fotos = fotos;
	}

	
	@JsonIgnore
	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	
	public List<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(List<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}
	
	public List<Desparasitacion> getDesparasitaciones() {
		return desparasitaciones;
	}

	public void setDesparasitaciones(List<Desparasitacion> desparasitaciones) {
		this.desparasitaciones = desparasitaciones;
	}

	public String getTamano() {
		return tamano;
	}

	public String getTipopeso() {
		return tipopeso;
	}

	public void setTipopeso(String tipopeso) {
		this.tipopeso = tipopeso;
	}



	public List<PesoMascota> getPeso() {
		return peso;
	}



	public void setPeso(List<PesoMascota> peso) {
		this.peso = peso;
	}
    
	
    
}

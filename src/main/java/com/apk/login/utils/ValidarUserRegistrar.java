package com.apk.login.utils;

public class ValidarUserRegistrar {
	
	private int existe;//0- Error 1- No existe 2- Existe mail y no phone 3- existe phone 4- existe mail y phone 5- ya hay un usuario registrado con mail o telefono 
	private int rol;
	private String perfil;
	
	public int getRol() {
		return rol;
	}
	public void setRol(int rol) {
		this.rol = rol;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public int getExiste() {
		return existe;
	}
	public void setExiste(int existe) {
		this.existe = existe;
	}
	
	

}

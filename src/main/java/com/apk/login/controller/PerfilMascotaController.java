package com.apk.login.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.Mascota;
import com.apk.login.service.PerfilMascotaService;
import com.google.gson.Gson;

import org.springframework.http.MediaType;


@RestController
public class PerfilMascotaController {

	public static final String ENCABEZADO = "Authorization";
	
	 @Autowired
	 PerfilMascotaService perfilMascotaService;
   

   /* @GetMapping("perfil-mascota")
    public ResponseEntity<String> obtenerPerfilPorId(@RequestParam("id") String id,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return perfilMascotaService.obtenerPerfilPorIdDueno(id, token);
    }*/
    
   
    @PostMapping(value="add-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarMascota(data, token);
       
    }
    
    @PostMapping(value="add-peso-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addPesoMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarPesoMascota(data, token);
       
    }
    
    @PostMapping(value="add-vacuna-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addVacunaMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarVacunaMascota(data, token);
       
    }
}

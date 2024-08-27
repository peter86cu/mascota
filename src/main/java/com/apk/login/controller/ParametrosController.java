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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.Mascota;
import com.apk.login.service.ParametroService;
import com.apk.login.service.PerfilMascotaService;
import com.google.gson.Gson;

import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/parameters")
public class ParametrosController {

	public static final String ENCABEZADO = "Authorization";
	
	 @Autowired
	 ParametroService parametrosService;
   

    @GetMapping("listado-tipo-raza")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> obtenerPerfilPorId(@RequestParam("tipo") String tipo,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return parametrosService.obtenerTipoRazas(tipo,token);
    }
    
    @GetMapping("listado-vacunas")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> obtenerVacunas(@RequestParam("tipo") String tipo,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return parametrosService.obtenerTiposVacunas(tipo,token);
    }
    
    @PostMapping(value="workSchedule",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return parametrosService.guardarCalendario(data, token);
       
    }
    
    
    @GetMapping("calendario-word-user")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> obtenerCalendarioWork(@RequestParam("userid") String userid,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return parametrosService.obtenerCalendarioWord(userid,token);
    }
    
    @GetMapping("activities")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> listadoActividadesDisponibles(HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return parametrosService.listadoActividadesDisponibles(token);
    }
    
}

package com.apk.login.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.Mascota;
import com.apk.login.service.EstilistaService;
import com.apk.login.service.ParametroService;
import com.apk.login.service.PerfilMascotaService;
import com.google.gson.Gson;

import org.springframework.http.MediaType;


@RestController
public class EstilistaController {

	public static final String ENCABEZADO = "Authorization";
	
	public static final String ADD = "All";
	
	 @Autowired
	 EstilistaService estilistaService;
   
	
	 
	/* @PostMapping
	    public ActividadEstilista createActivity(@RequestBody ActividadEstilista activity) {
		 ActividadEstilista savedActivity = estilistaService.saveActivity(activity);
	        messagingTemplate.convertAndSend("/topic/activities", savedActivity);
	        return savedActivity;
	    }
	 
	 @GetMapping("/user/{userId}")
	    public List<ActividadEstilista> getActivitiesByUserId(@PathVariable String userId) {
	        return activityService.getActivitiesByUserId(userId);
	    }*/
   
	 @PostMapping(value="add-evento",produces=MediaType.APPLICATION_JSON_VALUE)
	    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
		@ResponseStatus(HttpStatus.CREATED)
	    public ResponseEntity<?> addActividad(@RequestBody String data, HttpServletRequest request) {
	    	String token = request.getHeader(ENCABEZADO);
	    	boolean all = Boolean.parseBoolean(request.getHeader(ADD)) ;
	    	if(!all)
	    	return estilistaService.guardarActividad(data, token);
	    	return estilistaService.guardarActividadAll(data, token);
	       
	    }
	 
	 
    @GetMapping("activity-estilista")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> obtenerVacunas(@RequestParam("id") String id,@RequestParam("status") String status,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return estilistaService.obtenerActividad(id,token,status);
    }
    
    
 
    
}

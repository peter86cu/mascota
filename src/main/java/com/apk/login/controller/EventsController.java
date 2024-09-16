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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.Event;
import com.apk.login.modelo.Mascota;
import com.apk.login.service.EstilistaService;
import com.apk.login.service.EventService;
import com.apk.login.service.ParametroService;
import com.apk.login.service.PerfilMascotaService;
import com.google.gson.Gson;

import org.springframework.http.MediaType;

@RestController
public class EventsController {

	public static final String ENCABEZADO = "Authorization";

	public static final String ADD = "All";

	@Autowired
	EventService eventService;

	
	@PostMapping(value = "event-update", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateEventsLeido(@RequestBody Event data, @RequestHeader("Authorization") String token,@RequestHeader("mascotaId") String mascotaId) {
		
		return eventService.guardarEventoLeido(data, token,mascotaId);

	}
	
	@GetMapping(value = "/events/{petId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> obtenerEventosMascota(@PathVariable String petId,
			@RequestHeader("Authorization") String token) {
		// String token = request.getHeader(ENCABEZADO);
		return eventService.obtenerEventsByPet(petId, token);

	}

	

}

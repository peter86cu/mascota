package com.apk.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.Event;
import com.apk.login.modelo.Mascota;
import com.apk.login.repositorio.EventRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();

	 @Autowired
	 JwtTokenProvider jwt;

	 @Autowired
		PerfilMascotaRepository perfilMascotaRepository;
	 
    // Store the ID of the last known event to detect new ones
    private Long lastEventId = 0L;
    
 // Store the timestamp of the last check to detect updates
    private LocalDateTime lastCheckedTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // This method will be called every 5 seconds to check for new events
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void checkForNewEvents() {
        // Use the updated method to find new or updated events
        List<Event> newOrUpdatedEvents = eventRepository.findNewOrModifiedEvents(lastEventId, lastCheckedTime);

        if (!newOrUpdatedEvents.isEmpty()) {
            // Update the last known event ID and timestamp
            lastEventId = newOrUpdatedEvents.get(newOrUpdatedEvents.size() - 1).getId();
            lastCheckedTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS); // Ensure it is in UTC and truncated

            // Send the new or updated events to the WebSocket topic
            messagingTemplate.convertAndSend("/topic/events", newOrUpdatedEvents);
        }
    }
    
    
    
    public ResponseEntity<?> obtenerEventsByPet( String petId, String token) {
        try {
            if (token != null) {
                Claims claims = Jwts.parser()
                    .setSigningKey(jwt.key)
                    .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
                    .getBody();

                Date expiration = claims.getExpiration();
                if (expiration.before(new Date())) {
                    return new ResponseEntity<>("Expiró la sesión", HttpStatus.BAD_REQUEST);
                } else {
                	List<Event> album = eventRepository.obtenerEventsMascota(petId);
                		    
                    if (!album.isEmpty()) {
                        return ResponseEntity.ok(album);
                    } else {
                        // Devuelve 404 sin cambiar el tipo de retorno
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    }
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<?> guardarEventoLeido(Event evento, String token, String mascotaId){
      	try {
      		if (token != null) {
      			
      			// Se procesa el token y se recupera el usuario y los roles.
      			Claims claims = Jwts.parser()
                          .setSigningKey(jwt.key)
                          .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
                          .getBody();
    				//Claims claims = jwt.getUsernameFromToken(token);
    				Date authorities = claims.getExpiration();
      			
    				if (authorities.before(fecha)) {					
    					return new ResponseEntity<String>("Expiró la sección", HttpStatus.BAD_REQUEST);
    				} else {
    					Mascota mascota= perfilMascotaRepository.findById(mascotaId).get();
    					evento.setMascota(mascota);
    	    			return ResponseEntity.ok(eventRepository.save(evento));
    	        		
    				}
      			
      		}else {
      			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
      		}
      		  
      		
      		
      		//return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
    		} catch (Exception e) {
    			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
    		}
      }
}
package com.apk.login.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.Event;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.PesoMascota;
import com.apk.login.modelo.Vacuna;
import com.apk.login.repositorio.ActividadEstilistaRepository;
import com.apk.login.repositorio.EventsRepository;
import com.apk.login.repositorio.MascotaPesoRepository;
import com.apk.login.repositorio.MascotaVacunaRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class EstilistaService {

	public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();
	
	@Autowired
    ActividadEstilistaRepository actividadRepository;

	@Autowired
	EventsRepository eventsRepository;
	
	 @Value("${jwt.secret}")
	    private String secretKey;
	 
	 @Autowired
	 JwtTokenProvider jwt;
	 
	 
    
  public ResponseEntity<?> guardarActividad(String mascota, String token){
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
					// creamos el objeto con la información del usuario

	    			//ActividadEstilista perfil = new Gson().fromJson(mascota, ActividadEstilista.class);  
	    			ObjectMapper objectMapper = new ObjectMapper();
	    			ActividadEstilista perfil = objectMapper.readValue(mascota, ActividadEstilista.class);
	    			return ResponseEntity.ok(actividadRepository.save(perfil));
	        		//if(actividadRepository.save(perfil)!=null) {
	        		  // return new ResponseEntity<String>(new Gson().toJson(actividadRepository.save(perfil))   , HttpStatus.OK);	
	        		//}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
    		//return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
		}
    }
  
  
  public ResponseEntity<?> guardarEventoLeido(Event evento, String token){
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
					
	    			return ResponseEntity.ok(eventsRepository.save(evento));
	        		
				}
  			
  		}else {
  			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
  		}
  		  
  		
  		
  		//return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
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
              	List<Event> album = eventsRepository.obtenerEventsMascota(petId);
              		    
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
  
  
  public ResponseEntity<String> guardarActividadAll(String mascota, String token){
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
					// creamos el objeto con la información del usuario
					ObjectMapper objectMapper = new ObjectMapper();
					List<ActividadEstilista> actividades = objectMapper.readValue(mascota, new TypeReference<List<ActividadEstilista>>() {});
					boolean status=false;
					for (ActividadEstilista actividadEstilista : actividades) {
						if(actividadRepository.save(actividadEstilista)!=null) {
							status=true;
							
			        	}
					}
					
					if(status)
		        		   return new ResponseEntity<String>(new Gson().toJson("Evento guardado.")   , HttpStatus.OK);	
	        	return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error registrando las actividades. Intente de nuevo")   , HttpStatus.NOT_ACCEPTABLE);	

					
	    			//List<ActividadEstilista> perfil = new Gson().fromJson(mascota, List.class);    		
	        		
				}
  			
  		}else {
  			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
  		}
  		  
  		
  		
  		//return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.UNAUTHORIZED);	
		}
  }
  
    
    public ResponseEntity<?> obtenerActividad(String estilistaId, String token, String status){
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
					List<ActividadEstilista> actividad= new ArrayList<ActividadEstilista>();
				    if(status.equalsIgnoreCase("All")) {
				    	actividad= actividadRepository.obtenerAllActividadesEstilista(estilistaId);
				    }else {
				    	actividad= actividadRepository.obtenerActividadesEstilista(estilistaId, status);
				    }
				    
				    
				    if(!actividad.isEmpty()) {
 	         		   return ResponseEntity.ok(actividad);	

 				}else {
 	         		   return new ResponseEntity<String>(new Gson().toJson(actividad )   , HttpStatus.NO_CONTENT);	

 				}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.UNAUTHORIZED);	
		}
    }
    
    
    public ResponseEntity<?> obtenerActividadPorMascota(String mascotaId, String token, String status){
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
					
				    
				    List<ActividadEstilista> actividad= actividadRepository.obtenerActividadesMascota(mascotaId, status);
				    
				    if(!actividad.isEmpty()) {
 	         		   return ResponseEntity.ok(actividad);	

 				}else {
 	         		   return new ResponseEntity<String>(new Gson().toJson(actividad )   , HttpStatus.NO_CONTENT);	

 				}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.UNAUTHORIZED);	
		}
    }
    
    
    public ResponseEntity<?> actualizarEvento(String mascotaId, String token, String status){
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
					
				    
				    List<ActividadEstilista> actividad= actividadRepository.obtenerActividadesMascota(mascotaId, status);
				    
				    if(!actividad.isEmpty()) {
 	         		   return ResponseEntity.ok(actividad);	

 				}else {
 	         		   return new ResponseEntity<String>(new Gson().toJson(actividad )   , HttpStatus.NO_CONTENT);	

 				}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.UNAUTHORIZED);	
		}
    }
    
//    public ResponseEntity<String> actualizarActivity(int activityid, String status,String token){
//    	try {
//    		if (token != null) {
//    			
//    			// Se procesa el token y se recupera el usuario y los roles.
//    			Claims claims = Jwts.parser()
//                        .setSigningKey(jwt.key)
//                        .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
//                        .getBody();
//				//Claims claims = jwt.getUsernameFromToken(token);
//				Date authorities = claims.getExpiration();
//    			
//				if (authorities.before(fecha)) {					
//					return new ResponseEntity<String>("Expiró la sección", HttpStatus.BAD_REQUEST);
//				} else {
//					// creamos el objeto con la información del usuario
//
//	    			ActividadEstilista act = actividadRepository.;    		
//	        		if(perfilMascotaRepository.save(perfil)!=null) {
//	        		   return new ResponseEntity<String>(new Gson().toJson("Mascota agregada: "+ perfil.getNombre())   , HttpStatus.OK);	
//	        		}
//				}
//    			
//    		}else {
//    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
//    		}
//    		  
//    		
//    		
//    		return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
//		} catch (Exception e) {
//			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
//		}
//    }

    
}

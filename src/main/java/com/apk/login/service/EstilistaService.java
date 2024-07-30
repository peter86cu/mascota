package com.apk.login.service;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.PesoMascota;
import com.apk.login.modelo.Vacuna;
import com.apk.login.repositorio.ActividadEstilistaRepository;
import com.apk.login.repositorio.MascotaPesoRepository;
import com.apk.login.repositorio.MascotaVacunaRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
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

	
	
	 @Value("${jwt.secret}")
	    private String secretKey;
	 
	 @Autowired
	 JwtTokenProvider jwt;
	 
	 
    
  public ResponseEntity<String> guardarActividad(String mascota, String token){
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

	    			ActividadEstilista perfil = new Gson().fromJson(mascota, ActividadEstilista.class);    		
	        		if(actividadRepository.save(perfil)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("Evento guardado.")   , HttpStatus.OK);	
	        		}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
    		return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
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
					
				    
				    List<ActividadEstilista> actividad= actividadRepository.obtenerActividadesEstilista(estilistaId, status);
				    
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
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
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

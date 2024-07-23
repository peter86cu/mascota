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
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaPesoRepository;
import com.apk.login.modelo.MascotaVacunaRepository;
import com.apk.login.modelo.PerfilMascotaRepository;
import com.apk.login.modelo.PesoMascota;
import com.apk.login.modelo.Vacuna;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class PerfilMascotaService {

	public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();
	
	@Autowired
    PerfilMascotaRepository perfilMascotaRepository;

	
	@Autowired
	MascotaPesoRepository pesoMascotaRepository;
	
	@Autowired
	MascotaVacunaRepository vacunaMascotaRepository;
	
	 @Value("${jwt.secret}")
	    private String secretKey;
	 
	 @Autowired
	 JwtTokenProvider jwt;
	 
	 
    

    /*public ResponseEntity<String> obtenerPerfilPorIdDueno(String id, String token) {
    	
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
    			}else {
    				List<Mascota> perfilMascota = perfilMascotaRepository.findAllByUsuarioid(id);
    				if(!perfilMascota.isEmpty()) {
    	         		   return new ResponseEntity<String>(new Gson().toJson(perfilMascota)   , HttpStatus.OK);	

    				}else {
    	         		   return new ResponseEntity<String>(new Gson().toJson(perfilMascota )   , HttpStatus.NO_CONTENT);	

    				}

    			}
    			
        	}else {
        		return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
        	}
    		

    		
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización")  , HttpStatus.UNAUTHORIZED);	
		}
    	
    	
    	
    	
     }*/
    
    public ResponseEntity<String> guardarMascota(String mascota, String token){
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

	    			Mascota perfil = new Gson().fromJson(mascota, Mascota.class);    		
	        		if(perfilMascotaRepository.save(perfil)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("Mascota agregada: "+ perfil.getNombre())   , HttpStatus.OK);	
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
    
    public ResponseEntity<String> guardarPesoMascota(String datos, String token){
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
					JsonParser jsonParser = new JsonParser();
				    JsonObject payment_id = (JsonObject) jsonParser.parse(datos);
				    int id= payment_id.get("mascotaid").getAsInt();
				    
				    Mascota mGuarda= perfilMascotaRepository.findById(id).get();
				    
	    			PesoMascota perfil = new Gson().fromJson(datos, PesoMascota.class); 
	    			perfil.setMascota(mGuarda);
	    			
	        		if(pesoMascotaRepository.save(perfil)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("Peso de la mascota agregada.")   , HttpStatus.OK);	
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
    
    
    public ResponseEntity<String> guardarVacunaMascota(String datos, String token){
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
					JsonParser jsonParser = new JsonParser();
				    JsonObject payment_id = (JsonObject) jsonParser.parse(datos);
				    int id= payment_id.get("mascotaid").getAsInt();
				    
				    Mascota mGuarda= perfilMascotaRepository.findById(id).get();
				    
	    			Vacuna perfil = new Gson().fromJson(datos, Vacuna.class); 
	    			perfil.setMascota(mGuarda);
	    			
	        		if(vacunaMascotaRepository.save(perfil)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("Vacuna de la mascota guardada.")   , HttpStatus.OK);	
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
    
}

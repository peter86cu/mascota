package com.apk.login.service;

import java.util.ArrayList;
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
import com.apk.login.modelo.MascotaTemporal;
import com.apk.login.modelo.PesoMascota;
import com.apk.login.modelo.PesoMascotaTemporal;
import com.apk.login.modelo.Vacuna;
import com.apk.login.repositorio.MascotaPesoRepository;
import com.apk.login.repositorio.MascotaVacunaRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
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
	 
	 
    

   public ResponseEntity<?> obtenerPesoMascota(String mascotaId, String token) {
    	
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
    				List<PesoMascota> perfilMascota = pesoMascotaRepository.findAllPesoMascotas(mascotaId);
    				if(!perfilMascota.isEmpty()) {
    	         		   return ResponseEntity.ok(perfilMascota);	

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
    	
    	
    	
    	
     }
    
    public ResponseEntity<?> guardarMascota(String mascota, String token){
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

	    			MascotaTemporal perfil1 = new Gson().fromJson(mascota, MascotaTemporal.class); 
	    			Mascota perfil = new Mascota();
	    			perfil.setNombre(perfil1.getNombre());
	    			perfil.setRaza(perfil1.getRaza());
	    			perfil.setColor(perfil1.getColor());
	    			perfil.setComportamiento(perfil1.getComportamiento());
	    			perfil.setDesparasitaciones(null);
	    			perfil.setEdad(perfil1.getEdad());
	    			perfil.setEspecie(perfil1.getEspecie());
	    			perfil.setFechanacimiento(perfil1.getFechanacimiento());
	    			perfil.setFotos(perfil1.getFotos());
	    			perfil.setGenero(perfil1.getGenero());
	    			perfil.setHistorial_medico(perfil1.getHistorial_medico());
	    			perfil.setMascotaid(perfil1.getMascotaid());
	    			perfil.setNecesidades_especiales(perfil1.getNecesidades_especiales());
	    			perfil.setPersonalidad(perfil1.getPersonalidad());
	    			perfil.setTamano(perfil1.getTamano());
	    			perfil.setUsuario(perfil1.getUsuario());
	    			perfil.setVacunas(null);
	    			Mascota salvada= perfilMascotaRepository.save(perfil);
	        		if(salvada!=null) {
	        			if(!perfil1.getPesoMascota().isEmpty()) {
	        				MascotaTemporal response= new MascotaTemporal();
	        				PesoMascota peso= new PesoMascota();
		        			peso.setFecha(perfil1.getPesoMascota().get(0).getFecha());
		        			peso.setMascotaid(perfil.getMascotaid());
		        			peso.setPeso(perfil1.getPesoMascota().get(0).getPeso());
		        			peso.setPesoid(0);
		        			peso.setUm(perfil1.getPesoMascota().get(0).getUm());
		        			PesoMascota pesoSave=pesoMascotaRepository.save(peso);
		        			response.setNombre(salvada.getNombre());
		        			response.setRaza(salvada.getRaza());
		        			response.setColor(salvada.getColor());
		        			response.setComportamiento(salvada.getComportamiento());
		        			response.setDesparasitaciones(null);
		        			response.setEdad(salvada.getEdad());
		        			response.setEspecie(salvada.getEspecie());
		        			response.setFechanacimiento(salvada.getFechanacimiento());
		        			response.setFotos(salvada.getFotos());
		        			response.setGenero(salvada.getGenero());
		        			response.setHistorial_medico(salvada.getHistorial_medico());
		        			response.setMascotaid(salvada.getMascotaid());
		        			response.setNecesidades_especiales(salvada.getNecesidades_especiales());
		        			response.setPersonalidad(salvada.getPersonalidad());
		        			response.setTamano(salvada.getTamano());
		        			response.setUsuario(salvada.getUsuario());
		        			response.setVacunas(null);
			    			
		        			if(pesoSave!=null) {
		    	    			
		    	    			List<PesoMascotaTemporal> lstPeso= new ArrayList<PesoMascotaTemporal>();
		    	    			PesoMascotaTemporal pesoTem= new PesoMascotaTemporal();
		    	    			pesoTem.setFecha(pesoSave.getFecha());
		    	    			pesoTem.setMascotaid(pesoSave.getMascotaid());
		    	    			pesoTem.setPeso(pesoSave.getPeso());
		    	    			pesoTem.setUm(pesoSave.getUm());
		    	    			lstPeso.add(pesoTem);
		    	    			response.setPesoMascota(lstPeso);
		 	        		   return  ResponseEntity.ok(response);

		        			}else {
		        				perfilMascotaRepository.delete(perfil);
			 	        		return new ResponseEntity<String>(new Gson().toJson("Error salvando mascota"+ perfil.getNombre())   , HttpStatus.NOT_ACCEPTABLE);	

		        			}
	        			}else {
		 	        		   return  ResponseEntity.ok(salvada);	

	        			}
	        		
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
				    String id= payment_id.get("mascotaid").getAsString();
				    
				    Mascota mGuarda= perfilMascotaRepository.findById(id).get();
				    
	    			PesoMascota perfil = new Gson().fromJson(datos, PesoMascota.class); 
	    			perfil.setMascotaid(mGuarda.getMascotaid());
	    			
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
					/*JsonParser jsonParser = new JsonParser();
				    JsonObject payment_id = (JsonObject) jsonParser.parse(datos);
				    String id= payment_id.get("mascota").getAsString();
				    
				    Mascota mGuarda= perfilMascotaRepository.findById(id).get();*/
				    
	    			Vacuna perfil = new Gson().fromJson(datos, Vacuna.class); 
	    			//perfil.setMascota(mGuarda);
	    			
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
    
    
    /*public MascotaTemporal obtenerMascotaApp(Mascota mascota) {
    	MascotaTemporal response= new MascotaTemporal();
    	
    	
    	Mascota mGuarda= perfilMascotaRepository.findById(mascota.getMascotaid()).get();
    	if(mGuarda!=null) {
    		response.setNombre(mGuarda.getNombre());
    		response.setRaza(mGuarda.getRaza());
    		response.setColor(mGuarda.getColor());
    		response.setComportamiento(mGuarda.getComportamiento());
    		response.setDesparasitaciones(mGuarda.getDesparasitaciones());
    		response.setEdad(mGuarda.getEdad());
    		response.setEspecie(mGuarda.getEspecie());
    		response.setFechanacimiento(mGuarda.getFechanacimiento());
    		response.setFotos(mGuarda.getFotos());
    		response.setGenero(mGuarda.getGenero());
    		response.setHistorial_medico(mGuarda.getHistorial_medico());
    		response.setMascotaid(mGuarda.getMascotaid());
    		response.setNecesidades_especiales(mGuarda.getNecesidades_especiales());
    		response.setPersonalidad(mGuarda.getPersonalidad());
    		response.setTamano(mGuarda.getTamano());
    		response.setUsuario(mGuarda.getUsuario());
    		response.setVacunas(mGuarda.getVacunas());
    		
    		List<PesoMascota> peso= pesoMascotaRepository.findAllPesoMascotas(mGuarda.getMascotaid());
    		response.setPesoMascota(peso);
    	}
    	
    	return response;
    }*/
    
}

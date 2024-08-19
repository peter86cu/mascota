package com.apk.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.CommentResponse;
import com.apk.login.modelo.MascotaTemporal;
import com.apk.login.repositorio.CommentResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.List;

@Service
public class CommentResponseService {

    @Autowired
    private CommentResponseRepository commentResponseRepository;
    
    public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();
	
    
	 @Autowired
	 JwtTokenProvider jwt;
	 

    public List<CommentResponse> getResponsesByCommentId(String commentId) {
        return commentResponseRepository.findByComment(commentId);
    }

    public ResponseEntity<String> addCommentResponse(String response, String token, String commentId) {
    	
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
	    			//CommentResponse comment = new Gson().fromJson(response, CommentResponse.class); 

	    			CommentResponse comment = objectMapper.readValue(response, CommentResponse.class);

	        		if(commentResponseRepository.save(comment)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("OK")   , HttpStatus.OK);	
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

    public CommentResponse getResponseById(String id) {
        return commentResponseRepository.findById(id).orElse(null);
    }
}

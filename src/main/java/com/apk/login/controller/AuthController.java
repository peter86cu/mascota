package com.apk.login.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.AuthRequest;
import com.apk.login.modelo.AuthResponse;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.User;
import com.apk.login.service.UserService;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthController {
	
	 @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtTokenProvider jwtTokenProvider;

	    @Autowired
	    private UserService userService;

	    @PostMapping("login")
	    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
	        try {
	        	User userPlatafor= userService.validarUserPlataformaLogin(request.getUsername(), request.getPlataforma());
	        	if(userPlatafor!=null) {
	        		if(request.getPlataforma().contains("internet") && userPlatafor.getPlataforma().equalsIgnoreCase(request.getPlataforma())) {
		        		User user = userService.obtenerUserPorUserName(request.getUsername(), request.getPlataforma());
		        		if(user!=null) {
		        			if(user.getState()==1)
								return new ResponseEntity<String>(new Gson().toJson("Debe confirmar el correo para poder acceder."), HttpStatus.NOT_FOUND);

		        			if(!user.getPlataforma().equalsIgnoreCase(request.getPlataforma()))
								return new ResponseEntity<String>(new Gson().toJson("El usuario ."+user.getUsername()+" debe acceder con otra plataforma."), HttpStatus.NOT_ACCEPTABLE);

		        				
				        	String token = jwtTokenProvider.createToken(request.getUsername());
				        	List<Mascota> lstMacotas= new ArrayList<Mascota>();
				        	for(Mascota mascota : user.getMascotas()) {
				        		mascota.setUsuario(user);
				        		lstMacotas.add(mascota);
				        	}
				        	user.setMascotas(lstMacotas);
				        	AuthResponse response= new AuthResponse(user,token);
				            return ResponseEntity.ok(response);
		        		}else {
							return new ResponseEntity<String>(new Gson().toJson("Usuario no autorizado.."), HttpStatus.BAD_REQUEST);

		        		}
		        	}else if(request.getPlataforma().contains("manual") && userPlatafor.getPlataforma().equalsIgnoreCase(request.getPlataforma())){
		        		Authentication autentication = authenticationManager
								.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			        	if(autentication.isAuthenticated()) {
				        	User user=userService.authenticate(request.getUsername(), request.getPassword());
				        	if(user.getState()==1)
								return new ResponseEntity<String>(new Gson().toJson("Debe confirmar el correo para poder acceder."), HttpStatus.NOT_FOUND);

				        	if(!user.getPlataforma().equalsIgnoreCase(request.getPlataforma()))
								return new ResponseEntity<String>(new Gson().toJson("El usuario ."+user.getUsername()+" debe acceder con otra plataforma."), HttpStatus.NOT_ACCEPTABLE);

				        	
				        	String token = jwtTokenProvider.getToken(autentication);
				        	List<Mascota> lstMacotas= new ArrayList<Mascota>();
				        	for(Mascota mascota : user.getMascotas()) {
				        		mascota.setUsuario(user);
				        		lstMacotas.add(mascota);
				        	}
				        	user.setMascotas(lstMacotas);
				        	AuthResponse response= new AuthResponse(user,token);
				            return ResponseEntity.ok(response);

			        	}else {
		    				return new ResponseEntity<String>(new UsernameNotFoundException("Usuario o contraseña incorrecta.").toString(), HttpStatus.NOT_ACCEPTABLE);

			        	}
		        	}else {
						return new ResponseEntity<String>(new Gson().toJson("Usuario o contraseña incorrecta."), HttpStatus.NOT_ACCEPTABLE);

		        	}
	        	}else {
	        		if(request.getPlataforma().contains("internet") ) {
		        		User user = userService.obtenerUserPorUserName(request.getUsername(), request.getPlataforma());
		        		if(user!=null) {
		        			if(user.getState()==1)
								return new ResponseEntity<String>(new Gson().toJson("Debe confirmar el correo para poder acceder."), HttpStatus.NOT_FOUND);

		        			if(!user.getPlataforma().equalsIgnoreCase(request.getPlataforma()))
								return new ResponseEntity<String>(new Gson().toJson("El usuario ."+user.getUsername()+" debe acceder con otra plataforma."), HttpStatus.NOT_ACCEPTABLE);

		        				
				        	String token = jwtTokenProvider.createToken(request.getUsername());
				        	List<Mascota> lstMacotas= new ArrayList<Mascota>();
				        	for(Mascota mascota : user.getMascotas()) {
				        		mascota.setUsuario(user);
				        		lstMacotas.add(mascota);
				        	}
				        	user.setMascotas(lstMacotas);
				        	AuthResponse response= new AuthResponse(user,token);
				            return ResponseEntity.ok(response);
		        		}else {
							return new ResponseEntity<String>(new Gson().toJson("Usuario no autorizado.."), HttpStatus.BAD_REQUEST);

		        		}
		        	}else if(request.getPlataforma().contains("manual")){
		        		Authentication autentication = authenticationManager
								.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			        	if(autentication.isAuthenticated()) {
				        	User user=userService.authenticate(request.getUsername(), request.getPassword());
				        	if(user.getState()==1)
								return new ResponseEntity<String>(new Gson().toJson("Debe confirmar el correo para poder acceder."), HttpStatus.NOT_FOUND);

				        	if(!user.getPlataforma().equalsIgnoreCase(request.getPlataforma()))
								return new ResponseEntity<String>(new Gson().toJson("El usuario ."+user.getUsername()+" debe acceder con otra plataforma."), HttpStatus.NOT_ACCEPTABLE);

				        	
				        	String token = jwtTokenProvider.getToken(autentication);
				        	List<Mascota> lstMacotas= new ArrayList<Mascota>();
				        	for(Mascota mascota : user.getMascotas()) {
				        		mascota.setUsuario(user);
				        		lstMacotas.add(mascota);
				        	}
				        	user.setMascotas(lstMacotas);
				        	AuthResponse response= new AuthResponse(user,token);
				            return ResponseEntity.ok(response);

			        	}else {
		    				return new ResponseEntity<String>(new UsernameNotFoundException("Usuario o contraseña incorrecta.").toString(), HttpStatus.NOT_ACCEPTABLE);

			        	}
	        	   }else {
	   				return new ResponseEntity<String>(new Gson().toJson("Usuario o contraseña incorrecta."), HttpStatus.NO_CONTENT);

	        	   }
	        	}
	        } catch (AuthenticationException e) {
				return new ResponseEntity<String>(new Gson().toJson("Usuario o contraseña incorrecta."), HttpStatus.NO_CONTENT);

	        }
	    }
	    
	    @PostMapping("login-google")
	    public ResponseEntity<?>  generateTokenGoogle(@RequestParam("username") String username) {
	       return  ResponseEntity.ok(new Gson().toJson(jwtTokenProvider.createToken(username)) ); 
	    }
	    
	    @GetMapping("check-token")
	    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String token){
	    	
	    	if (token != null) {
	    		java.util.Date fecha = new Date();

	  			// Se procesa el token y se recupera el usuario y los roles.
	  			Claims claims = Jwts.parser()
	                      .setSigningKey(jwtTokenProvider.key)
	                      .parseClaimsJws(token.replace("Bearer ", ""))
	                      .getBody();
					//Claims claims = jwt.getUsernameFromToken(token);
					Date authorities = claims.getExpiration();
	  			
					if (authorities.before(fecha)) {					
						return ResponseEntity.ok(Collections.singletonMap("valido", false) ); 
					} else {
						
		    			return ResponseEntity.ok(Collections.singletonMap("valido", true));
		        		
					}
	  			
	  		}else {
	  			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
	  		}
	    	
	    	
	    	

	    }
	  //	    
//	    @PostMapping("obtener-user")
//	    public ResponseEntity<?> user(@RequestBody String request) {
//	        try {
//	        	
//	            String user = jwtTokenProvider.getUsernameFromToken(request);
//	            return ResponseEntity.ok(user);
//	        } catch (AuthenticationException e) {
//				return new ResponseEntity<String>(new UsernameNotFoundException("Invalid username/password supplied").toString(), HttpStatus.NOT_ACCEPTABLE);
//
//	        }
//	    }
	    
	    
	   


}

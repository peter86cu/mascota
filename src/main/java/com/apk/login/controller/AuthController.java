package com.apk.login.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	        	Authentication autentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
	        	if(autentication.isAuthenticated()) {
		        	User user=userService.authenticate(request.getUsername(), request.getPassword());
		        	if(user.getState()==1)
						return new ResponseEntity<String>(new Gson().toJson("Debe confirmar el correo para poder acceder."), HttpStatus.NOT_FOUND);

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
	        } catch (AuthenticationException e) {
				return new ResponseEntity<String>(new Gson().toJson("Usuario o contraseña incorrecta."), HttpStatus.NO_CONTENT);

	        }
	    }
	    
	    @PostMapping("login-google")
	    public ResponseEntity<?>  generateTokenGoogle(@RequestParam("username") String username) {
	       return  ResponseEntity.ok(new Gson().toJson(jwtTokenProvider.createToken(username)) ); 
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

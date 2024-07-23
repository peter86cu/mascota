package com.apk.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.apk.login.modelo.User;
import com.google.gson.Gson;

import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

	 @Value("${jwt.secret}")
	    private String secretKey;

	    @Value("${jwt.expiration}")
	    private long validityInMilliseconds;

	    
	    public Key key;

	    public JwtTokenProvider() {
	        // Generar una clave segura para HS256
	        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	    }
	    
	    
	    public String createToken(String username) {
	        Date now = new Date();
	        Date validity = new Date(now.getTime() + validityInMilliseconds);

	       return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(now)
	                .setExpiration(validity)
	                .signWith(key)
	                .compact();
	    }
	    
	    
	    public String getToken(Authentication autentication) {
			// en el body del token se incluye el usuario
			// y los roles a los que pertenece, además
			// de la fecha de caducidad y los datos de la firma
			String token = Jwts.builder().setIssuedAt(new Date()) // fecha creación
					.setSubject(autentication.getName())
					.claim("authorities", autentication.getAuthorities().stream() // roles
							.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
					.setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds)) // fecha caducidad
					.signWith(key)// clave y algoritmo para firma
					.compact(); // generación del token
			return token;
		}
	    
	    
	    public Claims getUsernameFromToken(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        return claims;
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	    
}

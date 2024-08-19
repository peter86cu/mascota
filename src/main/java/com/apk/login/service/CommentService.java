package com.apk.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.dto.CommentConverter;
import com.apk.login.dto.CommentDTO;
import com.apk.login.modelo.Comment;
import com.apk.login.repositorio.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ObjectMapper objectMapper; // Inyecta el ObjectMapper configurado
    
	public static final String PREFIJO_TOKEN = "Bearer ";

	@Value("${jwt.secret}")
	private String secretKey;

	@Autowired
	JwtTokenProvider jwt;
	
    java.util.Date fecha = new Date();


    public ResponseEntity<String> getCommentsByBusinessId(String businessId, String token) throws JsonProcessingException {
        try {
            if (token != null) {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwt.key)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody();
                Date expiration = claims.getExpiration();

                if (expiration.before(fecha)) {
                    return new ResponseEntity<>(objectMapper.writeValueAsString("Expiró la sesión"), HttpStatus.BAD_REQUEST);
                } else {
                    List<Comment> lstComment = commentRepository.findByBusinessId(businessId);
                    List<CommentDTO> dtoList = lstComment.stream()
                            .map(CommentConverter::toDTO)
                            .collect(Collectors.toList());
                    return new ResponseEntity<>(objectMapper.writeValueAsString(dtoList), HttpStatus.OK);
                }

            } else {
                return new ResponseEntity<>(objectMapper.writeValueAsString("Sin autorización."), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(objectMapper.writeValueAsString(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }
}

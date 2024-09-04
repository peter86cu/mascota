package com.apk.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.dto.CommentConverter;
import com.apk.login.dto.CommentDTO;
import com.apk.login.modelo.Business;
import com.apk.login.modelo.Comment;
import com.apk.login.repositorio.BusinessRepository;
import com.apk.login.repositorio.CommentRepository;
import com.apk.login.utils.CommentWS;
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
	private BusinessRepository businessRepository;
    
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



    public ResponseEntity<?> addComment(CommentWS comment, String token ) {
    	try {
			if (token != null) {

				// Se procesa el token y se recupera el usuario y los roles.
				Claims claims = Jwts.parser().setSigningKey(jwt.key).parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
						.getBody();
				// Claims claims = jwt.getUsernameFromToken(token);
				Date authorities = claims.getExpiration();

				if (authorities.before(fecha)) {
					return new ResponseEntity<String>("Expiró la sección", HttpStatus.BAD_REQUEST);
				} else {
					// creamos el objeto con la información del usuario

					Business negocio = businessRepository.obtenerNegocioCalificacion(comment.getActividadid());
					
					
					//return commentRepository.save(comment);
					if(negocio!=null) {
						Comment comentario= new Comment();
						comentario.setComment(comment.getComment());
						comentario.setId(comment.getId());
						comentario.setRating(comment.getRating());
						comentario.setTimestamp(comment.getTimestamp());
						comentario.setUser(comment.getUser());
						comentario.setResponses(comment.getResponses());
						comentario.setBusiness(negocio);
						comentario.setActividadid(comment.getActividadid());

						if ( commentRepository.save(comentario)!= null) {
							return  ResponseEntity.ok(commentRepository.save(comentario));
						}
					}else {
						return new ResponseEntity<String>(new Gson().toJson("Negocio no habilitado."), HttpStatus.ALREADY_REPORTED);

					}

					
					
				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error. Intente de nuevo"),
					HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error. Intente de nuevo"),
					HttpStatus.NOT_ACCEPTABLE);
		}
        
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }
}

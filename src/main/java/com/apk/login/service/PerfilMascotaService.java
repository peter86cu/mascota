package com.apk.login.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.Photos;
import com.apk.login.modelo.User;
import com.apk.login.modelo.UserAlbumLike;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaAlbun;
import com.apk.login.modelo.MascotaTemporal;
import com.apk.login.modelo.PesoMascota;
import com.apk.login.modelo.PesoMascotaTemporal;
import com.apk.login.modelo.Vacuna;
import com.apk.login.repositorio.AlbumMascotaRepository;
import com.apk.login.repositorio.MascotaPesoRepository;
import com.apk.login.repositorio.MascotaVacunaRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
import com.apk.login.repositorio.PhotoAlbumMascotaRepository;
import com.apk.login.repositorio.UserAlbumLikeRepository;
import com.apk.login.repositorio.UserRepository;
import com.apk.login.utils.LikeRequest;
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
	
	@Autowired
	AlbumMascotaRepository albumMascotaRepository;
	
	@Autowired
	PhotoAlbumMascotaRepository photoAlbumMascotaRepository;
	
	@Autowired
	UserAlbumLikeRepository userAlbumLikeRepository;
	
	 @Value("${jwt.secret}")
	    private String secretKey;
	 
	 @Autowired
	 JwtTokenProvider jwt;
	 
	 @Autowired
	 UserRepository userRepository;
    
	 @Autowired
	  private SimpMessagingTemplate messagingTemplate;

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
    
    
    public ResponseEntity<String> crearAlbumMascota(String datos, String token) throws org.springframework.dao.DataIntegrityViolationException{
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
					
	    			MascotaAlbun perfil = new Gson().fromJson(datos, MascotaAlbun.class); 
	    			//perfil.setMascota(mGuarda);
	    			
	        		if(albumMascotaRepository.save(perfil)!=null) {
	        		   return new ResponseEntity<String>(new Gson().toJson("Album creado.")   , HttpStatus.OK);	
	        		}
				}
    			
    		}else {
    			return new ResponseEntity<String>(new Gson().toJson ("Sin autorización"), HttpStatus.UNAUTHORIZED);
    		}
    		  
    		
    		
    		return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);
		}catch (org.springframework.dao.DataIntegrityViolationException e) {
		    // Manejar las violaciones de restricciones específicas
		    return new ResponseEntity<>(new Gson().toJson("Violación de restricciones: " + e.getMessage()), HttpStatus.BAD_REQUEST);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson ("Ocurrio un error. Intente de nuevo")  , HttpStatus.NOT_ACCEPTABLE);	
		}
    }
    
    public ResponseEntity<String> agregarFotoAlAlbum(String fotoBase64, String token) {
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
                	Photos foto = new Gson().fromJson(fotoBase64, Photos.class); 
                    //Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(foto.getAlbum());
	    			
	    			if(photoAlbumMascotaRepository.save(foto)!=null)
                    return new ResponseEntity<>("Foto añadida.", HttpStatus.OK);
                   /* if (albumOpt.isPresent()) {
                        MascotaAlbun album = albumOpt.get();
                        byte[] foto = Base64.getDecoder().decode(fotoBase64);
                        Photos nuevo= new Photos();
                        nuevo.setAlbum(albumOpt.get());
                        nuevo.setPhotoid(0);
                        nuevo.setPhoto(fotoBase64);
                        album.getPhotos().add(nuevo);
                        albumMascotaRepository.save(album);
                        return new ResponseEntity<>("Foto añadida.", HttpStatus.OK);
                    }*/
                    return new ResponseEntity<>("Álbum no encontrado.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al añadir foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<String> eliminarFotoDelAlbum( String albumId,  String photoId,  String token) {
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
                    Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(albumId);
                    if (albumOpt.isPresent()) {
                        MascotaAlbun album = albumOpt.get();
                        album.getPhotos().removeIf(foto -> foto.getPhotoid().equals(photoId));
                        albumMascotaRepository.save(album);
                        return new ResponseEntity<>("Foto eliminada.", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Álbum no encontrado.", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    public ResponseEntity<String> eliminarlAlbum( String albumId,   String token) {
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
                	 // Buscar el álbum por su ID
                    Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(albumId);
                    if (albumOpt.isPresent()) {
                        // Eliminar el álbum completo
                        albumMascotaRepository.delete(albumOpt.get());
                        return new ResponseEntity<>("Álbum eliminado exitosamente.", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Álbum no encontrado.", HttpStatus.NOT_FOUND);
                    }
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    public ResponseEntity<?> obtenerlAlbum( String mascotaId,   String token) {
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
                	Mascota mascota = perfilMascotaRepository.findById(mascotaId)
                		    .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

                	
                	List<MascotaAlbun> albumOpt = albumMascotaRepository.findByMascota(mascota);
                    if (!albumOpt.isEmpty()) {
                        return ResponseEntity.ok(albumOpt);
                    } else {
                        // Devuelve 404 sin cambiar el tipo de retorno
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    public ResponseEntity<?> obtenerlAlbumShared(  String token) {
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
                	List<MascotaAlbun> album = albumMascotaRepository.sharedAlbumIsSelected(true);
                		    
                    if (!album.isEmpty()) {
                        return ResponseEntity.ok(album);
                    } else {
                        // Devuelve 404 sin cambiar el tipo de retorno
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
    
    public ResponseEntity<?> isAlbumLikedByUser(String albumId,  String token, String userId) {
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
                	
                	Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(albumId);

                    if (!albumOpt.isPresent()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
                    }
                    User user=userRepository.findById(userId).get();
                    MascotaAlbun album = albumOpt.get();
                    Optional<UserAlbumLike> userLikeOpt = userAlbumLikeRepository.findByUserAndAlbum(user, album);

                    boolean isLiked = userLikeOpt.isPresent() && userLikeOpt.get().isLiked();

                    return ResponseEntity.ok(Collections.singletonMap("isLiked", isLiked));
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar foto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<?> actualizarAlbumsSeleccionados(String token, List<MascotaAlbun> albums) {
        try {
            // Verificar que el token no sea nulo
            if (token != null) {
                // Procesar el token JWT y obtener los detalles del usuario
                Claims claims = Jwts.parser()
                    .setSigningKey(jwt.key)
                    .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
                    .getBody();

                // Verificar si el token ha expirado
                Date expiration = claims.getExpiration();
                if (expiration.before(new Date())) {
                    return new ResponseEntity<>("Expiró la sesión", HttpStatus.BAD_REQUEST);
                } else {
                    // Actualizar directamente los álbumes proporcionados
                    for (MascotaAlbun album : albums) {
                        // Actualiza el campo isSelected en true
                        album.setSelected(true);
                        albumMascotaRepository.updateAlbumIsSelected(album.getId(), album.isSelected());

                        //albumMascotaRepository.save(album); // Guardar los cambios en la base de datos
                    }

                    // Retornar respuesta exitosa
                    return ResponseEntity.ok("Álbumes actualizados exitosamente.");
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar los álbumes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    public ResponseEntity<?> actualizarAlbumsLike(String token, String datos) {
        try {
            // Verificar que el token no sea nulo
            if (token != null) {
                // Procesar el token JWT y obtener los detalles del usuario
                Claims claims = Jwts.parser()
                    .setSigningKey(jwt.key)
                    .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
                    .getBody();

                // Verificar si el token ha expirado
                Date expiration = claims.getExpiration();
                if (expiration.before(new Date())) {
                    return new ResponseEntity<>("Expiró la sesión", HttpStatus.BAD_REQUEST);
                } else {
                	JsonParser jsonParser = new JsonParser();
				    JsonObject payment_id = (JsonObject) jsonParser.parse(datos);
				    String albumId= payment_id.get("albumId").getAsString();
				    int like = payment_id.get("liked").getAsInt();
				    boolean isLiked= payment_id.get("isLiked").getAsBoolean();
				    
                	 Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(albumId);
                     if (albumOpt.isPresent()) {
                    	 MascotaAlbun al= albumOpt.get();
                    	 int newLike=0;
                    	 if(!isLiked) {
                    		 newLike= al.getLikeCount()-like; 
                    	 }else {
                    		 newLike = like;
                    	 }
                    	 
                    	 albumMascotaRepository.updateAlbumIsLike(albumId, newLike, isLiked);
                     }

                    // Retornar respuesta exitosa
                    return ResponseEntity.ok("Álbumes actualizados exitosamente.");
                }
            } else {
                return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar los álbumes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    public ResponseEntity<?> likeOrUnlikeAlbum(@RequestBody LikeRequest likeRequest, String token) {
    	 try {
             // Verificar que el token no sea nulo
             if (token != null) {
                 // Procesar el token JWT y obtener los detalles del usuario
                 Claims claims = Jwts.parser()
                     .setSigningKey(jwt.key)
                     .parseClaimsJws(token.replace(PREFIJO_TOKEN, ""))
                     .getBody();

                 // Verificar si el token ha expirado
                 Date expiration = claims.getExpiration();
                 if (expiration.before(new Date())) {
                     return new ResponseEntity<>("Expiró la sesión", HttpStatus.BAD_REQUEST);
                 } else {
                	 Optional<MascotaAlbun> albumOpt = albumMascotaRepository.findById(likeRequest.getAlbumId());

                     if (!albumOpt.isPresent()) {
                         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
                     }

                     MascotaAlbun album = albumOpt.get();

                     // Buscar si el usuario ya ha dado like al álbum
                     Optional<UserAlbumLike> userLikeOpt = userAlbumLikeRepository.findByUserAndAlbum(likeRequest.getUser(), album);

                     if (userLikeOpt.isPresent()) {
                         UserAlbumLike userLike = userLikeOpt.get();
                         userLike.setLiked(likeRequest.isLiked());
                         userAlbumLikeRepository.save(userLike);
                     } else {
                         // Si es la primera vez que da like, crear un nuevo registro
                         UserAlbumLike newLike = new UserAlbumLike();
                         newLike.setUser(likeRequest.getUser());
                         newLike.setId(UUID.randomUUID().toString());
                         newLike.setAlbum(album);
                         newLike.setLiked(likeRequest.isLiked());
                         userAlbumLikeRepository.save(newLike);
                     }

                     // Actualizar el contador de likes en el álbum
                   /*  int likeCount = userAlbumLikeRepository.findLikeCountByAlbum(album);
                     
                     int newLike=0;
                	 if(!likeRequest.isLiked()) {
                		 newLike= likeCount-likeRequest.getLikeCount(); 
                	 }else {
                		 newLike = likeRequest.getLikeCount();
                	 }*/
                	 
                	// albumMascotaRepository.updateAlbumIsLike(albumId, newLike, isLiked);
                	 
                     album.setLikeCount(likeRequest.getLikeCount());
                     
                  // Enviar la actualización a todas las sesiones conectadas
                     messagingTemplate.convertAndSend("/topic/album/" + album.getId(), album.getLikeCount());

                     
                     albumMascotaRepository.save(album);

                     return ResponseEntity.ok("Like updated");
                 }
             } else {
                 return new ResponseEntity<>("Sin autorización", HttpStatus.UNAUTHORIZED);
             }
         } catch (Exception e) {
             return new ResponseEntity<>("Error al actualizar los álbumes", HttpStatus.INTERNAL_SERVER_ERROR);
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

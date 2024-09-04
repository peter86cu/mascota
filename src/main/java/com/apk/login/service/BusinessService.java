package com.apk.login.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.Business;
import com.apk.login.modelo.BusinessActivity;
import com.apk.login.modelo.TipoRaza;
import com.apk.login.modelo.User;
import com.apk.login.repositorio.BusinessRepository;
import com.apk.login.repositorio.BussinessActivityRepository;
import com.apk.login.repositorio.UserRepository;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class BusinessService {
	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BussinessActivityRepository bussinesActivityRepository;

	@Autowired
	private BussinessActivityRepository bussinessActivityRepository;

	@Value("${jwt.secret}")
	private String secretKey;

	@Autowired
	JwtTokenProvider jwt;

	public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();

	@Transactional
	public ResponseEntity<String> addBusiness(String userid, Business business, String token) {
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

					User user = userRepository.findById(userid)
							.orElseThrow(() -> new UsernameNotFoundException("User not found"));

					//Business savedBusiness = businessRepository.save(business);
					business.setUsuario(user);

					if ( businessRepository.save(business)!= null) {
						return new ResponseEntity<String>(new Gson().toJson("Evento guardado."), HttpStatus.OK);
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
	
	
	
	public ResponseEntity<String> addActivityBusiness(String userid, BusinessActivity business, String token) {
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

					Business negocio = businessRepository.obtenerNegocioPorUsuario(userid);
					
					if(negocio!=null) {
						business.setNegocio(negocio);

						if ( bussinessActivityRepository.save(business)!= null) {
							return new ResponseEntity<String>(new Gson().toJson("Actividad registrada."), HttpStatus.OK);
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
	
	
	public ResponseEntity<?> obtenerListaNegociosDisponibles(String busqueda, String token) {

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
					List<Business> negocios = businessRepository.filtrarListaNegocios(busqueda);
					if (!negocios.isEmpty()) {
						return  ResponseEntity.ok(negocios);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(negocios), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}
	
	
	public ResponseEntity<?> obtenerNegocioPorUsuario(String userId, String token) {

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
					Business negocio = businessRepository.obtenerNegocioPorUsuario(userId);
					if (negocio!=null) {
						return  ResponseEntity.ok(negocio);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(negocio), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}
	
	
	public ResponseEntity<?> obtenerNegocioParaCalificar(String actividadId, String token) {

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
					Business negocio = businessRepository.obtenerNegocioCalificacion(actividadId);
					if (negocio!=null) {
						return  ResponseEntity.ok(negocio);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(negocio), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}
	
	public ResponseEntity<?> obtenerListaActividadesPorNegocio(String negocioId , String token) {

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
					List<BusinessActivity> negocios = bussinesActivityRepository.obtenerActividadesNegocios(negocioId,"Disponible");
					if (!negocios.isEmpty()) {
						return  ResponseEntity.ok(negocios);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(negocios), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}
}
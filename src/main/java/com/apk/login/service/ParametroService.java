package com.apk.login.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.Activity;
import com.apk.login.modelo.CalendarioDay;
import com.apk.login.modelo.CalendarioWork;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.TipoRaza;
import com.apk.login.modelo.TipoVacunas;
import com.apk.login.repositorio.ActivityRepository;
import com.apk.login.repositorio.CalendarioDayRepository;
import com.apk.login.repositorio.CalendarioWordRepository;
import com.apk.login.repositorio.PerfilMascotaRepository;
import com.apk.login.repositorio.TipoRazaRepository;
import com.apk.login.repositorio.TipoVacunaRepository;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class ParametroService {

	public static final String PREFIJO_TOKEN = "Bearer ";
	java.util.Date fecha = new Date();

	@Autowired
	TipoRazaRepository tipoRazaRepository;

	@Autowired
	TipoVacunaRepository tipoVacunaRepository;

	@Autowired
	CalendarioWordRepository calendarioWorkRepository;

	@Autowired
	CalendarioDayRepository calendarioDayRepository;
	
	@Autowired
	ActivityRepository activityRepository;

	@Value("${jwt.secret}")
	private String secretKey;

	@Autowired
	JwtTokenProvider jwt;

	public ResponseEntity<String> obtenerTipoRazas(String tipo, String token) {

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
					List<TipoRaza> tiporaza = tipoRazaRepository.findAllByTipo(tipo);
					if (!tiporaza.isEmpty()) {
						return new ResponseEntity<String>(new Gson().toJson(tiporaza), HttpStatus.OK);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(tiporaza), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}

	public ResponseEntity<String> obtenerTiposVacunas(String tipoRaza, String token) {

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
					List<TipoVacunas> tiporaza = tipoVacunaRepository.findAllBytipoRaza(tipoRaza);
					if (!tiporaza.isEmpty()) {
						return new ResponseEntity<String>(new Gson().toJson(tiporaza), HttpStatus.OK);

					} else {
						return new ResponseEntity<String>(new Gson().toJson(tiporaza), HttpStatus.NO_CONTENT);

					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}

	public ResponseEntity<?> obtenerCalendarioWord(String usuarioId, String token) {

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
					CalendarioWork work = calendarioWorkRepository.obtenerCalendario(usuarioId);
					if (work != null) {
						List<CalendarioDay> calendari = calendarioDayRepository.obtenerCalendarioTrabajo(work.getId());
						if (!calendari.isEmpty()) {
							return ResponseEntity.ok(calendari);

						} else {
							return new ResponseEntity<String>(new Gson().toJson(calendari), HttpStatus.NO_CONTENT);

						}
					} else {
						return new ResponseEntity<String>(new Gson().toJson(work), HttpStatus.NO_CONTENT);
					}

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}

	public ResponseEntity<String> guardarCalendario(String datos, String token) {

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

					CalendarioDay calendario = new Gson().fromJson(datos, CalendarioDay.class);
					CalendarioWork calendarioWork = new CalendarioWork();
					calendarioWork = calendarioWorkRepository.save(calendario.getCalendario());
					calendario.setCalendario(calendarioWork);
					;
					if (calendarioDayRepository.save(calendario) != null)
						return new ResponseEntity<String>(new Gson().toJson("Calendario registrado "), HttpStatus.OK);

					return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error. Intente de nuevo"),
							HttpStatus.NOT_ACCEPTABLE);

				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}

	public ResponseEntity<?> listadoActividadesDisponibles(String token) {

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
					List<Activity> work = activityRepository.listadoActividadesActivas();					
							return ResponseEntity.ok(work);
				}

			} else {
				return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(new Gson().toJson("Sin autorización"), HttpStatus.UNAUTHORIZED);
		}

	}


}

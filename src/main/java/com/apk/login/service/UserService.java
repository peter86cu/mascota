package com.apk.login.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apk.login.JwtTokenProvider;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaTemporal;
import com.apk.login.modelo.User;
import com.apk.login.modelo.UserRoles;
import com.apk.login.repositorio.UserRepository;
import com.apk.login.utils.SendMail;
import com.ayalait.response.ResponseResultado;
import com.ayalait.utils.Email;
import com.ayalait.utils.ErrorState;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	PerfilMascotaService perfilMascota;
		
	@Autowired
    private JwtTokenProvider jwtTokenProvider;

	
	ErrorState error= new ErrorState();

	java.util.Date fecha = new Date();
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	
	
	
	 public User authenticate(String username, String password) throws AuthenticationException{
	        
		
			 User user = userRepository.findByUsername(username).get();
		        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
		        	      	
		            return user;
		        }
		        return null;
	    }
	 
	 
	 public ResponseEntity<String> registerUser(String news) {
		 
		 try {
			 User userTem= new Gson().fromJson(news, User.class);
			 User userExist = userRepository.findByUsername(userTem.getUsername()).get();
			 
			if( userExist.getRoles().stream()
		      .filter(rol-> rol.getId() == userTem.getRoles().get(0).getId())
		      .findAny()
		      .orElse(null)!=null) {
				
					
				 
				 User user= userRepository.save(userTem);
				 if(user!= null) {
					 //Crear tabla con las notificaciones para poder ser enviadas en otro momento si falla
					 ResponseResultado response= envioConfirmacion(user);
					 if(response.isStatus()) {
						 return new ResponseEntity<String>(new Gson().toJson(response.getResultado()),HttpStatus.OK);
					 }else {
						  return new ResponseEntity<String>(new Gson().toJson(response.getError().getMenssage()),HttpStatus.NOT_FOUND);

					 }
					
				 }				
				  return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error registrando al usuario."),HttpStatus.NOT_FOUND);
				
			}else {

				 if(userTem.getPlataforma().equalsIgnoreCase("manual")){
					 String pw1=new BCryptPasswordEncoder().encode(userTem.getPassword());
						userTem.setPassword(pw1);
				 }	
				 
				 User user= userRepository.save(userTem);
				 if(user!= null) {
					 //Crear tabla con las notificaciones para poder ser enviadas en otro momento si falla
					 ResponseResultado response= envioConfirmacion(user);
					 if(response.isStatus()) {
						 return new ResponseEntity<String>(new Gson().toJson(response.getResultado()),HttpStatus.OK);
					 }else {
						  return new ResponseEntity<String>(new Gson().toJson(response.getError().getMenssage()),HttpStatus.NOT_FOUND);

					 }
					
				 }				
				  return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error registrando al usuario."),HttpStatus.NOT_FOUND);

			}
			 
			 
			 
			 
			
		} catch(org.springframework.dao.DataIntegrityViolationException e) {
			
			  return new ResponseEntity<String>(new Gson().toJson("El correo/teléfono ya registrado."),HttpStatus.NOT_FOUND);

		}catch (Exception e) {
			  return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error registrando al usuario."),HttpStatus.NOT_FOUND);
		}
		 
		
	 }
	 
	 public ResponseEntity<String> validUserGoogle(String idUser) throws java.util.NoSuchElementException{
		 
		 try {
			 User user= userRepository.findById(idUser).get();
			 if(user!= null)
				 return new ResponseEntity<String>(new Gson().toJson("true"),HttpStatus.OK);
			  return new ResponseEntity<String>(new Gson().toJson("false"),HttpStatus.NOT_FOUND);
			
		} catch (Exception e) {
			  return new ResponseEntity<String>(new Gson().toJson("false"),HttpStatus.NOT_FOUND);

		}
		 
		
		 
	 }

	 public ResponseEntity<String> resetearPassword(String mail) {
		 User user= userRepository.findByUsername(mail).get();
		 if(user!=null) {
			 //genero token 
			String token = jwtTokenProvider.createToken(mail);
			ResponseResultado response= envioResetearPassword(user, token);
			 if(response.isStatus())
			     return new ResponseEntity<String>(new Gson().toJson("Se ha enviado un correo electrónico para recuperar su contraseña."),HttpStatus.OK);
			 return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error al enviar el correo, intente de nuevo."),HttpStatus.NOT_FOUND);

		 }else {
			  return new ResponseEntity<String>(new Gson().toJson("No existe nunguna cuenta registrada con el correo :" +mail),HttpStatus.NOT_FOUND);
 
		 }
			 
			 
	 }
	 
	 public ResponseEntity<String> confirmarNewPassword(String data) {
		 JsonParser jsonParser = new JsonParser();
		    JsonObject payment_id = (JsonObject) jsonParser.parse(data);
		    String id= payment_id.get("id").getAsString();
		    String token= payment_id.get("token").getAsString();
		    String pw1=payment_id.get("newPassword").getAsString();
		    String password=new BCryptPasswordEncoder().encode(pw1);
		   
		    if(jwtTokenProvider.validateToken(token)) {
				// Se procesa el token y se recupera el usuario y los roles.
				Claims claims = jwtTokenProvider.getUsernameFromToken(token);
				//Claims claims = jwt.getUsernameFromToken(token);
				Date authorities = claims.getExpiration();
				
				if (authorities.after(fecha)) {
					int  result= userRepository.confirmarNewPassword(id, password);
					 if(result>0) 
						return new ResponseEntity<String>(new Gson().toJson("Se actualizo la nueva contraseña."),HttpStatus.OK);
					return new ResponseEntity<String>(new Gson().toJson("Ocurrio un error al actualizar la contraseña, intente de nuevo."),HttpStatus.NOT_FOUND);
			 
				}
				return new ResponseEntity<String>(new Gson().toJson("El token ha caducado."),HttpStatus.NOT_FOUND);

		    
		    }
			return new ResponseEntity<String>(new Gson().toJson("El token no es valido."),HttpStatus.NOT_FOUND);

		 	 
	 }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ResponseResultado envioConfirmacion(User user) {
		Email confirmar= new Email();
		confirmar.setEmail(user.getUsername());
		confirmar.setName(user.getName());
		confirmar.setSubject("Confirmar cuenta PetCare+.");
		confirmar.setAdjunto(false);
		SendMail send=new SendMail();

		String mensajeEnvio=  " <html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Confirmación de Pedido</title>\r\n"
				+ "    <style>\r\n"
				+ "        /* Estilos para el botón */\r\n"
				+ "        .btn {\r\n"
				+ "            display: inline-block;\r\n"
				+ "            font-weight: 400;\r\n"
				+ "            color: #ffffff;\r\n"
				+ "            text-align: center;\r\n"
				+ "            vertical-align: middle;\r\n"
				+ "            user-select: none;\r\n"
				+ "            background-color: #007bff;\r\n"
				+ "            border: 1px solid transparent;\r\n"
				+ "            padding: 0.5rem 1rem;\r\n"
				+ "            font-size: 1rem;\r\n"
				+ "            line-height: 1.5;\r\n"
				+ "            border-radius: 0.25rem;\r\n"
				+ "            text-decoration: none;\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "        .btn:hover {\r\n"
				+ "            background-color: #0056b3;\r\n"
				+ "            color: #ffffff;\r\n"
				+ "        }\r\n"
				+ "    </style>\r\n"
				+ "</head>\r\n"
				+ "<body style=\"font-family: Arial, sans-serif; padding: 20px;\">\r\n"
				+ "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: auto;\">\r\n"
				+ "        <tr>\r\n"
				+ "            <td align=\"center\">\r\n"
				+ "                <h2 style=\"color: #007bff;\">Confirmación de usuario</h2>\r\n"
				+ "                <p>Estimado(a) "+user.getName()+",</p>\r\n"
				+ "                <p>Gracias por su registro en la aplicación PetCar+. Para hacer uso de la misma debe confirmar su correo. Por favor, haga clic en el siguiente botón para confirmar su usuario:</p>\r\n"
				+ "                <table cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\">\r\n"
				+ "                            <a class=\"btn\" href=\""+send.hostApp+"/confirmar-usuario?id="+user.getUserid()+"\" style=\"background-color: #007bff; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px; display: inline-block;\">Confirmar Pedido</a>\r\n"
				+ "                        </td>\r\n"
				+ "                    </tr>\r\n"
				+ "                </table>\r\n"
				+ "                <p>Si el botón no funciona, puede copiar y pegar el siguiente enlace en su navegador:</p>\r\n"
				+ "                <p><a href=\""+send.hostApp+"/confirmar-usuario?id="+user.getUserid()+"\" style=\"color: #007bff; text-decoration: underline;\">"+send.hostApp+"/confirmar-usuario?id="+user.getUserid()+"\"</a></p>\r\n"
				+ "                <p>Gracias,<br>Equipo de AYALA IT</p>\r\n"
				+ "					<p><img src=\"data:image/png;base64,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\" width=\"150\">\r\n"
				+ "      </p>\r\n"
				+ "            </td>\r\n"
				+ "        </tr>\r\n"
				+ "    </table>\r\n"
				+ "</body>\r\n"
				+ "</html>\r\n"
				+ " ";
		confirmar.setMessage(mensajeEnvio);
		return send.enviarMailConfirmacion(confirmar);

	}
	
	
	
	public String confirmarPedido(String idUser) {
		try {
			int response=userRepository.confirmarPedido(idUser);
			if(response>0) {
				return "<html lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>Confirmación Exitosa</title>\r\n"
						+ "    <style>\r\n"
						+ "        body {\r\n"
						+ "            font-family: Arial, sans-serif;\r\n"
						+ "            margin: 20px;\r\n"
						+ "            text-align: center;\r\n"
						+ "            background-color: #f3f3f3;\r\n"
						+ "        }\r\n"
						+ "        .container {\r\n"
						+ "            max-width: 600px;\r\n"
						+ "            margin: 0 auto;\r\n"
						+ "            padding: 20px;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            background-color: #ffffff;\r\n"
						+ "            box-shadow: 0 2px 4px rgba(0,0,0,0.1);\r\n"
						+ "        }\r\n"
						+ "        .icon {\r\n"
						+ "            font-size: 60px;\r\n"
						+ "            color: #28a745;\r\n"
						+ "        }\r\n"
						+ "        h2 {\r\n"
						+ "            color: #28a745;\r\n"
						+ "            margin-bottom: 10px;\r\n"
						+ "        }\r\n"
						+ "        p {\r\n"
						+ "            margin-bottom: 20px;\r\n"
						+ "        }\r\n"
						+ "        .btn {\r\n"
						+ "            display: inline-block;\r\n"
						+ "            font-weight: 400;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "            text-align: center;\r\n"
						+ "            vertical-align: middle;\r\n"
						+ "            user-select: none;\r\n"
						+ "            background-color: #007bff;\r\n"
						+ "            border: 1px solid transparent;\r\n"
						+ "            padding: 10px 20px;\r\n"
						+ "            font-size: 1rem;\r\n"
						+ "            line-height: 1.5;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            text-decoration: none;\r\n"
						+ "        }\r\n"
						+ "        .btn:hover {\r\n"
						+ "            background-color: #0056b3;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "        }\r\n"
						+ "    </style>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"container\">\r\n"
						+ "        <span class=\"icon\">&#10003;</span>\r\n"
						+ "        <h2>Confirmación Exitosa</h2>\r\n"
						+ "        <p>Su cuenta a sido confirmado con éxito. ¡Gracias por ser parte de PetCare+!</p>\r\n"						
						+ "    </div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n"
						+ "";

			}else {
				error.setCode(90004);
				error.setMenssage("Ocurrio un error actualizando su pedido.");
				return "<!DOCTYPE html>\r\n"
						+ "<html lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>No Confirmación de Pedido</title>\r\n"
						+ "    <style>\r\n"
						+ "        body {\r\n"
						+ "            font-family: Arial, sans-serif;\r\n"
						+ "            margin: 20px;\r\n"
						+ "            text-align: center;\r\n"
						+ "            background-color: #f3f3f3;\r\n"
						+ "        }\r\n"
						+ "        .container {\r\n"
						+ "            max-width: 600px;\r\n"
						+ "            margin: 0 auto;\r\n"
						+ "            padding: 20px;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            background-color: #ffffff;\r\n"
						+ "            box-shadow: 0 2px 4px rgba(0,0,0,0.1);\r\n"
						+ "        }\r\n"
						+ "        .icon {\r\n"
						+ "            font-size: 60px;\r\n"
						+ "            color: #dc3545;\r\n"
						+ "        }\r\n"
						+ "        h2 {\r\n"
						+ "            color: #dc3545;\r\n"
						+ "            margin-bottom: 10px;\r\n"
						+ "        }\r\n"
						+ "        p {\r\n"
						+ "            margin-bottom: 20px;\r\n"
						+ "        }\r\n"
						+ "        .btn {\r\n"
						+ "            display: inline-block;\r\n"
						+ "            font-weight: 400;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "            text-align: center;\r\n"
						+ "            vertical-align: middle;\r\n"
						+ "            user-select: none;\r\n"
						+ "            background-color: #007bff;\r\n"
						+ "            border: 1px solid transparent;\r\n"
						+ "            padding: 10px 20px;\r\n"
						+ "            font-size: 1rem;\r\n"
						+ "            line-height: 1.5;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            text-decoration: none;\r\n"
						+ "        }\r\n"
						+ "        .btn:hover {\r\n"
						+ "            background-color: #0056b3;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "        }\r\n"
						+ "    </style>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"container\">\r\n"
						+ "        <span class=\"icon\">&#10060;</span>\r\n"
						+ "        <h2>No se pudo Confirmar el Pedido</h2>\r\n"
						+ "        <p>Lamentablemente, no se ha podido confirmar su pedido en este momento.</p>\r\n"
						+ "        <p>Si tiene alguna pregunta o necesita asistencia, por favor contáctenos.</p>\r\n"
						+ "        <p>Gracias por su comprensión.</p>\r\n"
						+ "        <p><a class=\"btn\" href=\"https://ayalait.com.uy/contactanos/\" target=\"_blank\">Contactar Soporte</a></p>\r\n"
						+ "    </div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n"
						+ "";

			}
			
		} catch (Exception e) {
			error.setCode(90020);
			error.setMenssage(e.getCause().getMessage());
			return "<!DOCTYPE html>\r\n"
					+ "<html lang=\"en\">\r\n"
					+ "<head>\r\n"
					+ "    <meta charset=\"UTF-8\">\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "    <title>No Confirmación de Pedido</title>\r\n"
					+ "    <style>\r\n"
					+ "        body {\r\n"
					+ "            font-family: Arial, sans-serif;\r\n"
					+ "            margin: 20px;\r\n"
					+ "            text-align: center;\r\n"
					+ "            background-color: #f3f3f3;\r\n"
					+ "        }\r\n"
					+ "        .container {\r\n"
					+ "            max-width: 600px;\r\n"
					+ "            margin: 0 auto;\r\n"
					+ "            padding: 20px;\r\n"
					+ "            border-radius: 5px;\r\n"
					+ "            background-color: #ffffff;\r\n"
					+ "            box-shadow: 0 2px 4px rgba(0,0,0,0.1);\r\n"
					+ "        }\r\n"
					+ "        .icon {\r\n"
					+ "            font-size: 60px;\r\n"
					+ "            color: #dc3545;\r\n"
					+ "        }\r\n"
					+ "        h2 {\r\n"
					+ "            color: #dc3545;\r\n"
					+ "            margin-bottom: 10px;\r\n"
					+ "        }\r\n"
					+ "        p {\r\n"
					+ "            margin-bottom: 20px;\r\n"
					+ "        }\r\n"
					+ "        .btn {\r\n"
					+ "            display: inline-block;\r\n"
					+ "            font-weight: 400;\r\n"
					+ "            color: #ffffff;\r\n"
					+ "            text-align: center;\r\n"
					+ "            vertical-align: middle;\r\n"
					+ "            user-select: none;\r\n"
					+ "            background-color: #007bff;\r\n"
					+ "            border: 1px solid transparent;\r\n"
					+ "            padding: 10px 20px;\r\n"
					+ "            font-size: 1rem;\r\n"
					+ "            line-height: 1.5;\r\n"
					+ "            border-radius: 5px;\r\n"
					+ "            text-decoration: none;\r\n"
					+ "        }\r\n"
					+ "        .btn:hover {\r\n"
					+ "            background-color: #0056b3;\r\n"
					+ "            color: #ffffff;\r\n"
					+ "        }\r\n"
					+ "    </style>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "    <div class=\"container\">\r\n"
					+ "        <span class=\"icon\">&#10060;</span>\r\n"
					+ "        <h2>No se pudo Confirmar el Pedido</h2>\r\n"
					+ "        <p>Lamentablemente, no se ha podido confirmar su pedido en este momento.</p>\r\n"
					+ "        <p>Si tiene alguna pregunta o necesita asistencia, por favor contáctenos.</p>\r\n"
					+ "        <p>Gracias por su comprensión.</p>\r\n"
					+ "        <p><a class=\"btn\" href=\"https://ayalait.com.uy/contactanos\" target=\"_blank\">Contactar Soporte</a></p>\r\n"
					+ "    </div>\r\n"
					+ "</body>\r\n"
					+ "</html>\r\n"
					+ "";
		}
	}
	
	public ResponseResultado envioResetearPassword(User user, String token ) {
		
		Email confirmar= new Email();
		confirmar.setEmail(user.getUsername());
		confirmar.setName(user.getName());
		confirmar.setSubject("Resetear password PetCare+");
		confirmar.setAdjunto(false);
		SendMail send=new SendMail();

		String mensajeEnvio= "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <style>\r\n"
				+ "        body {\r\n"
				+ "            font-family: Arial, sans-serif;\r\n"
				+ "            background-color: #f4f4f4;\r\n"
				+ "            margin: 0;\r\n"
				+ "            padding: 0;\r\n"
				+ "        }\r\n"
				+ "        .container {\r\n"
				+ "            background-color: #ffffff;\r\n"
				+ "            margin: 50px auto;\r\n"
				+ "            padding: 20px;\r\n"
				+ "            border-radius: 10px;\r\n"
				+ "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
				+ "            max-width: 600px;\r\n"
				+ "        }\r\n"
				+ "        .header {\r\n"
				+ "            text-align: center;\r\n"
				+ "            padding: 10px 0;\r\n"
				+ "            border-bottom: 1px solid #e0e0e0;\r\n"
				+ "        }\r\n"
				+ "        .header h1 {\r\n"
				+ "            margin: 0;\r\n"
				+ "            font-size: 24px;\r\n"
				+ "            color: #333333;\r\n"
				+ "        }\r\n"
				+ "        .content {\r\n"
				+ "            padding: 20px;\r\n"
				+ "        }\r\n"
				+ "        .content p {\r\n"
				+ "            font-size: 16px;\r\n"
				+ "            color: #333333;\r\n"
				+ "            line-height: 1.5;\r\n"
				+ "        }\r\n"
				+ "        .button {\r\n"
				+ "            display: block;\r\n"
				+ "            width: 200px;\r\n"
				+ "            margin: 20px auto;\r\n"
				+ "            padding: 10px 0;\r\n"
				+ "            background-color: #007bff;\r\n"
				+ "            color: #ffffff;\r\n"
				+ "            text-align: center;\r\n"
				+ "            text-decoration: none;\r\n"
				+ "            border-radius: 5px;\r\n"
				+ "            font-size: 16px;\r\n"
				+ "        }\r\n"
				+ "        .footer {\r\n"
				+ "            text-align: center;\r\n"
				+ "            padding: 10px 0;\r\n"
				+ "            border-top: 1px solid #e0e0e0;\r\n"
				+ "            font-size: 12px;\r\n"
				+ "            color: #777777;\r\n"
				+ "        }\r\n"
				+ "    </style>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "        <div class=\"header\">\r\n"
				+ "            <h1>Restablecer Contraseña</h1>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"content\">\r\n"
				+ "            <p>Hola "+user.getName()+",</p>\r\n"
				+ "            <p>Hemos recibido una solicitud para restablecer tu contraseña. Si no realizaste esta solicitud, por favor ignora este correo electrónico.</p>\r\n"
				+ "            <p>Para restablecer tu contraseña, haz clic en el siguiente enlace:</p>\r\n"
				+ "            <a href=\""+send.hostApp+"/formulario-reseteo-password?id="+user.getUserid()+"&token="+token+"\" class=\"button\">Restablecer Contraseña</a>\r\n"
				+ "            <p>Este enlace es válido por 24 horas.</p>\r\n"
				+ "            <p>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>\r\n"
				+ "            <p>Saludos,<br>El equipo de [Nombre de la Compañía]</p>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"footer\">\r\n"
				+ "            <p>© [Año] [Nombre de la Compañía]. Todos los derechos reservados.</p>\r\n"
				+ "        </div>\r\n"
				+ "    </div>\r\n"
				+ "</body>\r\n"
				+ "</html>\r\n"
				+ "";
		
		confirmar.setMessage(mensajeEnvio);
		return send.enviarMailConfirmacion(confirmar);
	}

	
	public String formularioReseteoPassword(String idUser, String token) {
		
		if(jwtTokenProvider.validateToken(token)) {
			// Se procesa el token y se recupera el usuario y los roles.
			Claims claims = jwtTokenProvider.getUsernameFromToken(token);
			//Claims claims = jwt.getUsernameFromToken(token);
			Date authorities = claims.getExpiration();
			
			if (authorities.after(fecha)) {
				return "<!DOCTYPE html>\r\n"
						+ "<html lang=\"es\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>Restablecer Contraseña</title>\r\n"
						+ "    <style>\r\n"
						+ "        body {\r\n"
						+ "            font-family: Arial, sans-serif;\r\n"
						+ "            background-color: #f4f4f4;\r\n"
						+ "            margin: 0;\r\n"
						+ "            padding: 0;\r\n"
						+ "        }\r\n"
						+ "        .container {\r\n"
						+ "            max-width: 400px;\r\n"
						+ "            background-color: #ffffff;\r\n"
						+ "            margin: 50px auto;\r\n"
						+ "            padding: 20px;\r\n"
						+ "            border-radius: 10px;\r\n"
						+ "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
						+ "        }\r\n"
						+ "        h1 {\r\n"
						+ "            text-align: center;\r\n"
						+ "            color: #333333;\r\n"
						+ "        }\r\n"
						+ "        .form-group {\r\n"
						+ "            margin-bottom: 15px;\r\n"
						+ "        }\r\n"
						+ "        .form-group label {\r\n"
						+ "            display: block;\r\n"
						+ "            font-size: 14px;\r\n"
						+ "            color: #333333;\r\n"
						+ "            margin-bottom: 5px;\r\n"
						+ "        }\r\n"
						+ "        .form-group input {\r\n"
						+ "            width: 100%;\r\n"
						+ "            padding: 10px;\r\n"
						+ "            font-size: 16px;\r\n"
						+ "            border: 1px solid #cccccc;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "        }\r\n"
						+ "        .button {\r\n"
						+ "            width: 100%;\r\n"
						+ "            padding: 10px;\r\n"
						+ "            background-color: #007bff;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "            border: none;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            font-size: 16px;\r\n"
						+ "            cursor: pointer;\r\n"
						+ "        }\r\n"
						+ "        .button:hover {\r\n"
						+ "            background-color: #0056b3;\r\n"
						+ "        }\r\n"
						+ "        .error-message {\r\n"
						+ "            color: red;\r\n"
						+ "            font-size: 14px;\r\n"
						+ "            display: none;\r\n"
						+ "        }\r\n"
						+ "        .success-message {\r\n"
						+ "            color: green;\r\n"
						+ "            font-size: 14px;\r\n"
						+ "            display: none;\r\n"
						+ "        }\r\n"
						+ "    </style>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"container\">\r\n"
						+ "        <h1>Restablecer Contraseña</h1>\r\n"
						+ "        <form id=\"reset-password-form\">\r\n"
						+ "           \r\n"
						+ "            <div class=\"form-group\">\r\n"
						+ "                <label for=\"new-password\">Nueva Contraseña</label>\r\n"
						+ "                <input type=\"password\" id=\"new-password\" name=\"new-password\" required>\r\n"
						+ "            </div>\r\n"
						+ "            <div class=\"form-group\">\r\n"
						+ "                <label for=\"confirm-password\">Confirmar Nueva Contraseña</label>\r\n"
						+ "                <input type=\"password\" id=\"confirm-password\" name=\"confirm-password\" required>\r\n"
						+ "            </div>\r\n"
						+ "            <p id=\"error-message\" class=\"error-message\">Las contraseñas no coinciden</p>\r\n"
						+ "            <p id=\"success-message\" class=\"success-message\">Contraseña restablecida con éxito</p>\r\n"
						+ "            <button type=\"submit\" class=\"button\">Restablecer Contraseña</button>\r\n"
						+ "        </form>\r\n"
						+ "    </div>\r\n"
						+ "\r\n"
						+ "    <script>\r\n"
						+ "        document.getElementById('reset-password-form').addEventListener('submit', function(event) {\r\n"
						+ "            event.preventDefault(); // Evitar el envío del formulario para validación\r\n"
						+ "\r\n"
						+ " const urlParams = new URLSearchParams(window.location.search);\r\n"
						+ "				// Obtener el valor del parámetro 'id'\r\n"
						+ "			var id = urlParams.get('id');\r\n"
						+ "            var token =  urlParams.get('token');\r\n"
						+ "            var newPassword = document.getElementById('new-password').value;\r\n"
						+ "            var confirmPassword = document.getElementById('confirm-password').value;\r\n"
						+ "            var errorMessage = document.getElementById('error-message');\r\n"
						+ "            var successMessage = document.getElementById('success-message');\r\n"
						+ "\r\n"
						+ "            if (newPassword === confirmPassword) {\r\n"
						+ "                errorMessage.style.display = 'none';\r\n"
						+ "\r\n"
						+ "                // Enviar solicitud AJAX para restablecer la contraseña\r\n"
						+ "                var xhr = new XMLHttpRequest();\r\n"
						+ "                xhr.open('POST', 'http://192.168.0.154:8080/confirmar-reseteo-password', true);\r\n"
						+ "                xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');\r\n"
						+ "\r\n"
						+ "                xhr.onload = function() {\r\n"
						+ "                    if (xhr.status === 200) {\r\n"
						+ "                        successMessage.style.display = 'block';\r\n"
						+ "                        errorMessage.style.display = 'none';\r\n"
						+ "						newPassword.value = \"\";\r\n"
						+ "						confirmPassword.value = \"\";\r\n"
						+ "						\r\n"
						+ "                    } else {\r\n"
						+ "                        errorMessage.textContent = 'Token inválido o expirado';\r\n"
						+ "                        errorMessage.style.display = 'block';\r\n"
						+ "                        successMessage.style.display = 'none';\r\n"
						+ "                    }\r\n"
						+ "                };\r\n"
						+ "\r\n"
						+ "                xhr.onerror = function() {\r\n"
						+ "                    errorMessage.textContent = 'Error de red. Por favor, inténtalo de nuevo.';\r\n"
						+ "                    errorMessage.style.display = 'block';\r\n"
						+ "                    successMessage.style.display = 'none';\r\n"
						+ "                };\r\n"
						+ "\r\n"
						+ "                xhr.send(JSON.stringify({ token: token, id:id, newPassword: newPassword }));\r\n"
						+ "            } else {\r\n"
						+ "                errorMessage.textContent = 'Las contraseñas no coinciden';\r\n"
						+ "                errorMessage.style.display = 'block';\r\n"
						+ "                successMessage.style.display = 'none';\r\n"
						+ "            }\r\n"
						+ "        });\r\n"
						+ "    </script>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n"
						+ "";
			}else {
				return "<!DOCTYPE html>\r\n"
						+ "<html lang=\"es\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>Error de Token Vencido</title>\r\n"
						+ "    <style>\r\n"
						+ "        body {\r\n"
						+ "            font-family: Arial, sans-serif;\r\n"
						+ "            background-color: #f4f4f4;\r\n"
						+ "            margin: 0;\r\n"
						+ "            padding: 0;\r\n"
						+ "            display: flex;\r\n"
						+ "            justify-content: center;\r\n"
						+ "            align-items: center;\r\n"
						+ "            height: 100vh;\r\n"
						+ "        }\r\n"
						+ "        .container {\r\n"
						+ "            max-width: 400px;\r\n"
						+ "            background-color: #ffffff;\r\n"
						+ "            padding: 20px;\r\n"
						+ "            border-radius: 10px;\r\n"
						+ "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
						+ "            text-align: center;\r\n"
						+ "        }\r\n"
						+ "        h1 {\r\n"
						+ "            color: #ff0000;\r\n"
						+ "        }\r\n"
						+ "        p {\r\n"
						+ "            font-size: 16px;\r\n"
						+ "            color: #333333;\r\n"
						+ "        }\r\n"
						+ "        .button {\r\n"
						+ "            margin-top: 20px;\r\n"
						+ "            padding: 10px 20px;\r\n"
						+ "            background-color: #007bff;\r\n"
						+ "            color: #ffffff;\r\n"
						+ "            border: none;\r\n"
						+ "            border-radius: 5px;\r\n"
						+ "            font-size: 16px;\r\n"
						+ "            cursor: pointer;\r\n"
						+ "            text-decoration: none;\r\n"
						+ "        }\r\n"
						+ "        .button:hover {\r\n"
						+ "            background-color: #0056b3;\r\n"
						+ "        }\r\n"
						+ "    </style>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"container\">\r\n"
						+ "        <h1>Token Vencido</h1>\r\n"
						+ "        <p>Tu enlace de restablecimiento de contraseña ha expirado.</p>\r\n"
						+ "        <p>Por favor, solicita un nuevo enlace de restablecimiento.</p>\r\n"
						+ "        <a href=\"/solicitar-nuevo-enlace\" class=\"button\">Solicitar Nuevo Enlace</a>\r\n"
						+ "    </div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n"
						+ "";
			}
		}else {
			return "<!DOCTYPE html>\r\n"
					+ "<html lang=\"es\">\r\n"
					+ "<head>\r\n"
					+ "    <meta charset=\"UTF-8\">\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "    <title>Error de Token Vencido</title>\r\n"
					+ "    <style>\r\n"
					+ "        body {\r\n"
					+ "            font-family: Arial, sans-serif;\r\n"
					+ "            background-color: #f4f4f4;\r\n"
					+ "            margin: 0;\r\n"
					+ "            padding: 0;\r\n"
					+ "            display: flex;\r\n"
					+ "            justify-content: center;\r\n"
					+ "            align-items: center;\r\n"
					+ "            height: 100vh;\r\n"
					+ "        }\r\n"
					+ "        .container {\r\n"
					+ "            max-width: 400px;\r\n"
					+ "            background-color: #ffffff;\r\n"
					+ "            padding: 20px;\r\n"
					+ "            border-radius: 10px;\r\n"
					+ "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
					+ "            text-align: center;\r\n"
					+ "        }\r\n"
					+ "        h1 {\r\n"
					+ "            color: #ff0000;\r\n"
					+ "        }\r\n"
					+ "        p {\r\n"
					+ "            font-size: 16px;\r\n"
					+ "            color: #333333;\r\n"
					+ "        }\r\n"
					+ "        .button {\r\n"
					+ "            margin-top: 20px;\r\n"
					+ "            padding: 10px 20px;\r\n"
					+ "            background-color: #007bff;\r\n"
					+ "            color: #ffffff;\r\n"
					+ "            border: none;\r\n"
					+ "            border-radius: 5px;\r\n"
					+ "            font-size: 16px;\r\n"
					+ "            cursor: pointer;\r\n"
					+ "            text-decoration: none;\r\n"
					+ "        }\r\n"
					+ "        .button:hover {\r\n"
					+ "            background-color: #0056b3;\r\n"
					+ "        }\r\n"
					+ "    </style>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "    <div class=\"container\">\r\n"
					+ "        <h1>Token Vencido</h1>\r\n"
					+ "        <p>Tu enlace de restablecimiento de contraseña ha expirado.</p>\r\n"
					+ "        <p>Por favor, solicita un nuevo enlace de restablecimiento.</p>\r\n"
					+ "        <a href=\"/solicitar-nuevo-enlace\" class=\"button\">Solicitar Nuevo Enlace</a>\r\n"
					+ "    </div>\r\n"
					+ "</body>\r\n"
					+ "</html>\r\n"
					+ "";
		}
		
		
		
		
		
		
	}
}


package com.apk.login.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.Mascota;
import com.apk.login.service.PerfilMascotaService;
import com.apk.login.service.UserService;
import com.google.gson.Gson;

import org.springframework.http.MediaType;


@RestController
public class UserController {

	public static final String ENCABEZADO = "Authorization";
	
	 @Autowired
	 UserService userService;
   

   
    @PostMapping(value="create-user",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addUser(@RequestBody String data ){
    	return userService.registerUser(data);
       
    }
    

    @GetMapping("validar-google")
    public ResponseEntity<String> obtenerPerfilPorId(@RequestParam("id") String id) {    	
       return userService.validUserGoogle(id);
    }
    
    @GetMapping(value="confirmar-usuario")
	public String confirmarPedido(@RequestParam("id") String id) {		
		return userService.confirmarPedido(id);
	}
    
    @GetMapping(value="formulario-reseteo-password")
	public String formularioReseteo(@RequestParam("id") String id, @RequestParam("token") String token) {		
		return userService.formularioReseteoPassword(id,token);
	}
    
    @GetMapping(value="resetear-password")
	public ResponseEntity<String> resetearPassword(@RequestParam("email") String email) {		
		return userService.resetearPassword(email);
	}
    
    
    @PostMapping(value="confirmar-reseteo-password")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> confirmarReseteoPassword(@RequestBody String data ) {		
		return userService.confirmarNewPassword(data);
	}
}

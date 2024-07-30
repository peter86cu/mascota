package com.apk.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.Business;
import com.apk.login.service.BusinessService;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {
    @Autowired
    private BusinessService businessService;
    
	public static final String ENCABEZADO = "Authorization";


    @PostMapping(value="add-businesses",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBusiness(@RequestParam String userid, @RequestBody Business business,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
        return ResponseEntity.ok(businessService.addBusiness(userid, business,token));
    }
    
    @GetMapping("list-businesses")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> obtenerVacunas(@RequestParam("search") String search,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return businessService.obtenerListaNegociosDisponibles(search,token);
    }
    
    
    @GetMapping("list-activity-businesses")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
   	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> obtenerActividadPorNegocio(@RequestParam("negocioId") String negocioId,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return businessService.obtenerListaActividadesPorNegocio(negocioId,token);
    }
}
package com.apk.login.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaAlbun;
import com.apk.login.modelo.User;
import com.apk.login.service.PerfilMascotaService;
import com.apk.login.utils.LikeRequest;
import com.google.gson.Gson;

import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/pet")
public class PerfilMascotaController {

	public static final String ENCABEZADO = "Authorization";
	
	 @Autowired
	 PerfilMascotaService perfilMascotaService;
   

    @GetMapping("peso-mascota")
    public ResponseEntity<?> obtenerPerfilPorId(@RequestParam("id") String id,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return perfilMascotaService.obtenerPesoMascota(id, token);
    }
    
    @GetMapping("albums")
    public ResponseEntity<?> obtenerAlbumMascota(@RequestParam("petId") String id,HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
       return perfilMascotaService.obtenerlAlbum(id, token);
    }
    
    @GetMapping(value="albums/{albumId}/isLiked" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> isAlbumLikedByUser(@PathVariable String albumId, @RequestHeader("Authorization") String token, @RequestHeader("userId") String userId) {
    	//String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.isAlbumLikedByUser(albumId,token, userId);
       
    }
    
    @GetMapping("albums-shared")
    public ResponseEntity<?> obtenerAlbumMascotaCompartidos(@RequestHeader("Authorization") String token) {
       return perfilMascotaService.obtenerlAlbumShared(token);
    }
   
    @PostMapping(value="add-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarMascota(data, token);
       
    }
 
    @PostMapping(value="/albums",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crearAlbumMascota(@RequestBody String datos, @RequestHeader("Authorization") String token) {
    	//String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.crearAlbumMascota(datos, token);
       
    }
    
    @PostMapping(value="/albums/photos",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> agregarPhotoAlbumMascota(@RequestBody String fotoBase64, @RequestHeader("Authorization") String token) {
    	//String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.agregarFotoAlAlbum(fotoBase64, token);
       
    }
    
    @DeleteMapping(value="/albums/{albumId}/photos/{photoId}" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> eliminarPhotoAlbumMascota(@PathVariable String albumId, @PathVariable String photoId, @RequestHeader("Authorization") String token) {
    	//String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.eliminarFotoDelAlbum(albumId,photoId, token);
       
    }
    
   

    @DeleteMapping(value="/albums/{albumId}" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> eliminarAlbumMascota(@PathVariable String albumId, @RequestHeader("Authorization") String token) {
    	//String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.eliminarlAlbum(albumId, token);
       
    }
    
   /* @PostMapping("/albums/likes")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> actualizarAlbumsLike(@RequestHeader("Authorization") String token,@RequestBody String datos) {
        
        return perfilMascotaService.actualizarAlbumsLike( token,datos);
    }*/
    
    
    @PostMapping("/albums/likes")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> actualizarAlbumsLikeNew(@RequestBody LikeRequest likeRequest, @RequestHeader("Authorization") String token) {
        
        return perfilMascotaService.likeOrUnlikeAlbum(likeRequest,token);
    }
    
    
    @PostMapping("/albums/share")
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?>actualizarAlbumsCompartidos (@RequestHeader("Authorization") String token,@RequestBody List<MascotaAlbun> albums) {
        
        return perfilMascotaService.actualizarAlbumsSeleccionados( token, albums);
    }
    
    @PostMapping(value="add-peso-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addPesoMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarPesoMascota(data, token);
       
    }
    
    @PostMapping(value="add-vacuna-mascota",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addVacunaMascota(@RequestBody String data, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	return perfilMascotaService.guardarVacunaMascota(data, token);
       
    }
}

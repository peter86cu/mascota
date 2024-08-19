package com.apk.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apk.login.modelo.CommentResponse;
import com.apk.login.service.CommentResponseService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/responses")
public class CommentResponseController {
	
	public static final String ENCABEZADO = "Authorization";
	
	public static final String COMMENT = "commentId";



    @Autowired
    private CommentResponseService commentResponseService;

    @GetMapping("/comment/{commentId}")
    public List<CommentResponse> getResponsesByCommentId(@PathVariable String commentId) {
        return commentResponseService.getResponsesByCommentId(commentId);
    }

    @PostMapping(value="/comment/new",produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addCommentResponse(@RequestBody String response, HttpServletRequest request) {
    	String token = request.getHeader(ENCABEZADO);
    	String commentId = request.getHeader(COMMENT);
        return commentResponseService.addCommentResponse(response,token, commentId);
    }
}

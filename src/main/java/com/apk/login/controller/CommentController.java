package com.apk.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apk.login.modelo.Comment;
import com.apk.login.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	public static final String ENCABEZADO = "Authorization";

	
    @Autowired
    private CommentService commentService;

    @GetMapping("/business/{businessId}")
    public  ResponseEntity<?> getCommentsByBusinessId(@PathVariable String businessId,HttpServletRequest request) throws JsonProcessingException {
    	String token = request.getHeader(ENCABEZADO);
        return commentService.getCommentsByBusinessId(businessId, token);
    }

    @PostMapping
    public Comment addComment(@RequestBody Comment comment,HttpServletRequest request) {
        return commentService.addComment(comment);
    }
}


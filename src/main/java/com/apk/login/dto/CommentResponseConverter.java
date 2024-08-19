package com.apk.login.dto;

import org.hibernate.Hibernate;

import com.apk.login.modelo.CommentResponse;

public class CommentResponseConverter {
    public static CommentResponseDTO toDTO(CommentResponse response) {
        CommentResponseDTO dto = new CommentResponseDTO();
        Hibernate.initialize(response.getUser());

        dto.setUser(response.getUser());
        dto.setCommentId(response.getComment());
        dto.setResponse(response.getRespuesta());
        dto.setTimestamp(response.getTimestamp());
  
return dto;
    }
}

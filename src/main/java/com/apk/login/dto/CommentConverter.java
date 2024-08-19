package com.apk.login.dto;

import java.util.stream.Collectors;

import org.hibernate.Hibernate;

import com.apk.login.modelo.Comment;

public class CommentConverter {
    public static CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        Hibernate.initialize(comment.getUser());
        dto.setUser(comment.getUser());
        dto.setComment(comment.getComment());
        dto.setRating(comment.getRating());
        dto.setTimestamp(comment.getTimestamp());
        dto.setResponses(comment.getResponses().stream()
            .map(CommentResponseConverter::toDTO)
            .collect(Collectors.toList()));
        return dto;
    }
}




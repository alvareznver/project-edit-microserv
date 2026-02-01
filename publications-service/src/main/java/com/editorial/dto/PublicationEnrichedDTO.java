package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PublicationEnrichedDTO - Para enviar datos de publicacion CON datos del autor
 * Patron: Enriquecer con informacion de otro microservicio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEnrichedDTO {

    private Long id;
    private String title;
    private String description;
    private Long authorId;
    private AuthorDTO author;
    private String status;
    private String category;
    private String publicationType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDTO {
        private Long id;
        private String name;
        private String email;
        private String country;
    }
}

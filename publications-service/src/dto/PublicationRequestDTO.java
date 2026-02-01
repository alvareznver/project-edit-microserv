package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PublicationRequestDTO - Para recibir datos de creación de publicación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequestDTO {
    
    private String title;
    private String description;
    private String content;
    private Long authorId;
    private String category;
}

/**
 * PublicationResponseDTO - Para enviar datos de publicación (sin autor enriquecido)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private Long authorId;
    private String status;
    private String category;
    private String publicationType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

/**
 * PublicationEnrichedDTO - Para enviar datos de publicación CON datos del autor
 * Patrón: Enriquecer con información de otro microservicio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEnrichedDTO {
    
    private Long id;
    private String title;
    private String description;
    private Long authorId;
    private AuthorDTO author; // Datos del autor enriquecidos
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

/**
 * PublicationStatusUpdateDTO - Para cambiar estado de publicación
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationStatusUpdateDTO {
    
    private String status;
}
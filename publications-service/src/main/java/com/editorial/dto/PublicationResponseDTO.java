package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PublicationResponseDTO - Para enviar datos de publicacion (sin autor enriquecido)
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

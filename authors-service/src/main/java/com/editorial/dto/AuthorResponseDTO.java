package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuthorResponseDTO - Para enviar datos de salida
 * Principio SRP: Responsabilidad unica de serializar respuestas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String bio;
    private String country;
    private String authorType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

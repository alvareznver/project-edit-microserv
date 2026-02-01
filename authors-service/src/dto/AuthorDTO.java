package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuthorRequestDTO - Para recibir datos de entrada
 * Principio DIP: La API no expone la entidad directamente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequestDTO {
    
    private String name;
    private String email;
    private String bio;
    private String country;
}

/**
 * AuthorResponseDTO - Para enviar datos de salida
 * Principio SRP: Responsabilidad única de serializar respuestas
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

/**
 * AuthorExistsDTO - Para respuesta de verificación de existencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorExistsDTO {
    
    private boolean exists;
    private Long id;
    private String name;
}
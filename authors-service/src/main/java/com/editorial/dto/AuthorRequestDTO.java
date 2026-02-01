package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

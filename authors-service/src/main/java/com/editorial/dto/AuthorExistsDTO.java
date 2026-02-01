package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthorExistsDTO - Para respuesta de verificacion de existencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorExistsDTO {

    private boolean exists;
    private Long id;
    private String name;
}

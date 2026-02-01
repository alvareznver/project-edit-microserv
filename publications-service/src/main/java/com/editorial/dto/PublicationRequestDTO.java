package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublicationRequestDTO - Para recibir datos de creacion de publicacion
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

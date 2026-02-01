package com.editorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublicationStatusUpdateDTO - Para cambiar estado de publicacion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationStatusUpdateDTO {

    private String status;
}

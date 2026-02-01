package com.editorial.exception;

/**
 * PublicationNotFoundException - Publicacion no encontrada
 */
public class PublicationNotFoundException extends RuntimeException {
    public PublicationNotFoundException(String message) {
        super(message);
    }
}

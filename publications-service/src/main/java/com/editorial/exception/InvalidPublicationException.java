package com.editorial.exception;

/**
 * InvalidPublicationException - Datos de publicacion invalidos
 */
public class InvalidPublicationException extends RuntimeException {
    public InvalidPublicationException(String message) {
        super(message);
    }
}

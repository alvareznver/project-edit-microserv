package com.editorial.exception;

/**
 * AuthorAlreadyExistsException - Excepcion cuando autor ya existe
 */
public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}

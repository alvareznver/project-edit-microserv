package com.editorial.exception;

/**
 * AuthorNotFoundException - Excepcion cuando autor no es encontrado
 */
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

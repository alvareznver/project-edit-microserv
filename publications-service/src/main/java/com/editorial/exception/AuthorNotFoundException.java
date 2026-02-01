package com.editorial.exception;

/**
 * AuthorNotFoundException - Autor no existe en Authors Service
 */
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}

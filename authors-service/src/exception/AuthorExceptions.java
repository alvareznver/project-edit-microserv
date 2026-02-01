package com.editorial.exception;

/**
 * AuthorNotFoundException - Excepción cuando autor no es encontrado
 */
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * AuthorAlreadyExistsException - Excepción cuando autor ya existe
 */
public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}

/**
 * InvalidAuthorException - Excepción cuando datos de autor son inválidos
 */
public class InvalidAuthorException extends RuntimeException {
    public InvalidAuthorException(String message) {
        super(message);
    }
}
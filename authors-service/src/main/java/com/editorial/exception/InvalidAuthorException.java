package com.editorial.exception;

/**
 * InvalidAuthorException - Excepcion cuando datos de autor son invalidos
 */
public class InvalidAuthorException extends RuntimeException {
    public InvalidAuthorException(String message) {
        super(message);
    }
}

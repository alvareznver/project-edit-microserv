package com.editorial.exception;

/**
 * PublicationNotFoundException - Publicación no encontrada
 */
public class PublicationNotFoundException extends RuntimeException {
    public PublicationNotFoundException(String message) {
        super(message);
    }
}

/**
 * InvalidPublicationException - Datos de publicación inválidos
 */
public class InvalidPublicationException extends RuntimeException {
    public InvalidPublicationException(String message) {
        super(message);
    }
}

/**
 * AuthorNotFoundException - Autor no existe en Authors Service
 */
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}

/**
 * InvalidStateTransitionException - Transición de estado no permitida
 */
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
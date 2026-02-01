package com.editorial.exception;

/**
 * InvalidStateTransitionException - Transicion de estado no permitida
 */
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

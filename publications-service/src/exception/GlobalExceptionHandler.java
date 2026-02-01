package com.editorial.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Manejo centralizado de excepciones
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePublicationNotFound(PublicationNotFoundException ex) {
        log.error("Publicación no encontrada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "PUBLICATION_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorNotFound(AuthorNotFoundException ex) {
        log.error("Autor no encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "AUTHOR_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidPublicationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPublication(InvalidPublicationException ex) {
        log.error("Publicación inválida: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_PUBLICATION", ex.getMessage());
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStateTransition(InvalidStateTransitionException ex) {
        log.error("Transición de estado no permitida: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_STATE_TRANSITION", ex.getMessage());
    }

    @ExceptionHandler(AuthorServiceClient.AuthorServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorServiceException(
            AuthorServiceClient.AuthorServiceException ex) {
        log.error("Error en Authors Service: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "AUTHOR_SERVICE_ERROR", 
                "Error al comunicarse con el servicio de autores");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error no manejado: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", 
                "Error interno del servidor");
    }

    /**
     * Construir respuesta de error estándar
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", errorCode);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
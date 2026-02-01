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
 * Aplicación de principios SOLID:
 * - SRP: Responsabilidad única de manejo de errores
 * - OCP: Extensible sin modificar código existente
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorNotFound(AuthorNotFoundException ex) {
        log.error("Autor no encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "AUTHOR_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorAlreadyExists(AuthorAlreadyExistsException ex) {
        log.error("Autor ya existe: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "AUTHOR_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(InvalidAuthorException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAuthor(InvalidAuthorException ex) {
        log.error("Autor inválido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_AUTHOR", ex.getMessage());
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
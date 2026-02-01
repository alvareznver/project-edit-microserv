package com.editorial.client;

import com.editorial.dto.PublicationEnrichedDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * AuthorServiceClient - Cliente HTTP para Authors Service
 * Patrón Adapter: Adapta las llamadas HTTP a la interfaz esperada
 * 
 * Aplicación de patrones:
 * - Adapter: Adaptador HTTP para comunicación inter-servicios
 * - Strategy: Estrategia de validación remota
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceClient {

    private final RestTemplate restTemplate;

    @Value("${authors.service.url:http://localhost:8080}")
    private String authorsServiceUrl;

    @Value("${authors.service.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${authors.service.read-timeout:10000}")
    private int readTimeout;

    /**
     * Verificar si autor existe en Authors Service
     * @param authorId ID del autor a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean authorExists(Long authorId) {
        if (authorId == null || authorId <= 0) {
            log.warn("ID de autor inválido: {}", authorId);
            return false;
        }

        try {
            String url = authorsServiceUrl + "/api/authors/" + authorId + "/exists";
            log.debug("Verificando existencia de autor en: {}", url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("exists")) {
                boolean exists = (boolean) response.get("exists");
                log.info("Resultado verificación de autor {}: {}", authorId, exists);
                return exists;
            }

            log.warn("Respuesta inválida del servicio de autores");
            return false;

        } catch (RestClientException e) {
            log.error("Error conectando con Authors Service para verificar autor {}: {}", 
                    authorId, e.getMessage());
            // En caso de error de conexión, lanzar excepción para que el cliente lo maneje
            throw new AuthorServiceException("No se pudo verificar la existencia del autor. " +
                    "El servicio de autores no está disponible", e);
        } catch (Exception e) {
            log.error("Error inesperado al verificar autor {}: {}", authorId, e.getMessage());
            throw new AuthorServiceException("Error inesperado al verificar autor", e);
        }
    }

    /**
     * Obtener datos del autor desde Authors Service
     * @param authorId ID del autor
     * @return Datos del autor enriquecido
     */
    public PublicationEnrichedDTO.AuthorDTO getAuthorData(Long authorId) {
        if (authorId == null || authorId <= 0) {
            log.warn("ID de autor inválido para obtener datos: {}", authorId);
            return null;
        }

        try {
            String url = authorsServiceUrl + "/api/authors/" + authorId;
            log.debug("Obteniendo datos de autor en: {}", url);

            PublicationEnrichedDTO.AuthorDTO author = restTemplate.getForObject(url, 
                    PublicationEnrichedDTO.AuthorDTO.class);

            log.info("Datos de autor obtenidos exitosamente para ID: {}", authorId);
            return author;

        } catch (RestClientException e) {
            log.error("Error conectando con Authors Service para obtener datos del autor {}: {}", 
                    authorId, e.getMessage());
            return null; // Retornar null en lugar de fallar la operación
        } catch (Exception e) {
            log.error("Error inesperado al obtener datos del autor {}: {}", authorId, e.getMessage());
            return null;
        }
    }

    /**
     * Excepción personalizada para errores de Authors Service
     */
    public static class AuthorServiceException extends RuntimeException {
        public AuthorServiceException(String message) {
            super(message);
        }

        public AuthorServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
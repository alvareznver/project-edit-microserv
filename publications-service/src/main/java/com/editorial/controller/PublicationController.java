package com.editorial.controller;

import com.editorial.dto.PublicationEnrichedDTO;
import com.editorial.dto.PublicationRequestDTO;
import com.editorial.dto.PublicationResponseDTO;
import com.editorial.dto.PublicationStatusUpdateDTO;
import com.editorial.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PublicationController - REST Controller para gestión de publicaciones
 */
@RestController
@RequestMapping("/api/publications")
@RequiredArgsConstructor
@Slf4j
public class PublicationController {

    private final PublicationService publicationService;

    /**
     * POST /api/publications - Crear nueva publicación
     * Valida existencia de autor llamando a Authors Service
     */
    @PostMapping
    public ResponseEntity<PublicationResponseDTO> createPublication(
            @RequestBody PublicationRequestDTO requestDTO) {
        log.info("POST /api/publications - Crear publicación: {}", requestDTO.getTitle());
        PublicationResponseDTO response = publicationService.createPublication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/publications/{id} - Obtener publicación (versión enriquecida)
     * Incluye datos del autor si están disponibles
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublicationEnrichedDTO> getPublication(@PathVariable Long id) {
        log.info("GET /api/publications/{} - Obtener publicación", id);
        PublicationEnrichedDTO response = publicationService.getPublicationEnriched(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/publications - Listar publicaciones con filtros
     * @param page Número de página (default: 0)
     * @param size Tamaño de página (default: 10)
     * @param status Filtro por estado editorial (opcional)
     * @param authorId Filtro por autor (opcional)
     */
    @GetMapping
    public ResponseEntity<Page<PublicationResponseDTO>> listPublications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long authorId) {
        log.info("GET /api/publications - Listar publicaciones");
        Page<PublicationResponseDTO> response = publicationService.listPublications(page, size, status, authorId);
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/publications/{id}/status - Cambiar estado de publicación
     * Valida transiciones de estado permitidas
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<PublicationResponseDTO> updatePublicationStatus(
            @PathVariable Long id,
            @RequestBody PublicationStatusUpdateDTO statusUpdate) {
        log.info("PATCH /api/publications/{}/status - Cambiar estado a: {}", id, statusUpdate.getStatus());
        PublicationResponseDTO response = publicationService.updatePublicationStatus(id, statusUpdate);
        return ResponseEntity.ok(response);
    }
}
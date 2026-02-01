package com.editorial.service;

import com.editorial.client.AuthorServiceClient;
import com.editorial.dto.PublicationEnrichedDTO;
import com.editorial.dto.PublicationRequestDTO;
import com.editorial.dto.PublicationResponseDTO;
import com.editorial.dto.PublicationStatusUpdateDTO;
import com.editorial.entity.Publication;
import com.editorial.entity.PublicationImpl;
import com.editorial.exception.*;
import com.editorial.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PublicationService - Servicio de Publicaciones
 * Aplicación de patrones:
 * - Facade: Orquesta PublicationRepository y AuthorServiceClient
 * - Strategy: Validaciones de transición de estado
 * - DIP: Depende de abstracciones (Repository, Client)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final AuthorServiceClient authorServiceClient;

    /**
     * Crear nueva publicación
     * Valida que el autor exista en Authors Service antes de crear
     */
    public PublicationResponseDTO createPublication(PublicationRequestDTO requestDTO) {
        log.info("Creando nueva publicación: {}", requestDTO.getTitle());

        // Validar datos requeridos
        if (requestDTO.getTitle() == null || requestDTO.getTitle().isBlank()) {
            throw new InvalidPublicationException("El título es requerido");
        }

        if (requestDTO.getAuthorId() == null || requestDTO.getAuthorId() <= 0) {
            throw new InvalidPublicationException("ID de autor es requerido");
        }

        // LLAMADA A AUTHORS SERVICE - Validar existencia de autor
        try {
            if (!authorServiceClient.authorExists(requestDTO.getAuthorId())) {
                log.warn("Intento de crear publicación con autor inexistente: {}", 
                        requestDTO.getAuthorId());
                throw new AuthorNotFoundException("Autor con ID " + requestDTO.getAuthorId() + 
                        " no existe");
            }
        } catch (AuthorServiceClient.AuthorServiceException e) {
            log.error("Error al validar autor: {}", e.getMessage());
            throw new AuthorNotFoundException("No se pudo validar la existencia del autor: " + 
                    e.getMessage());
        }

        // Crear publicación
        PublicationImpl publication = new PublicationImpl(
                requestDTO.getTitle(),
                requestDTO.getDescription(),
                requestDTO.getContent(),
                requestDTO.getAuthorId(),
                requestDTO.getCategory()
        );

        // Guardar en BD
        PublicationImpl savedPublication = publicationRepository.save(publication);
        log.info("Publicación creada exitosamente con ID: {}", savedPublication.getId());

        return mapToResponseDTO(savedPublication);
    }

    /**
     * Obtener publicación por ID (sin enriquecer)
     */
    @Transactional(readOnly = true)
    public PublicationResponseDTO getPublicationById(Long id) {
        log.info("Obteniendo publicación con ID: {}", id);

        PublicationImpl publication = publicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Publicación no encontrada con ID: {}", id);
                    return new PublicationNotFoundException("Publicación con ID " + id + 
                            " no encontrada");
                });

        return mapToResponseDTO(publication);
    }

    /**
     * Obtener publicación enriquecida CON datos del autor
     * Llama a Authors Service para obtener información del autor
     */
    @Transactional(readOnly = true)
    public PublicationEnrichedDTO getPublicationEnriched(Long id) {
        log.info("Obteniendo publicación enriquecida con ID: {}", id);

        PublicationImpl publication = publicationRepository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException("Publicación con ID " + id + 
                        " no encontrada"));

        // Obtener datos del autor desde Authors Service
        PublicationEnrichedDTO.AuthorDTO authorData = 
                authorServiceClient.getAuthorData(publication.getAuthorId());

        // Mapear a DTO enriquecido
        return mapToEnrichedDTO(publication, authorData);
    }

    /**
     * Listar publicaciones con paginación y filtros opcionales
     */
    @Transactional(readOnly = true)
    public Page<PublicationResponseDTO> listPublications(
            int page, int size, String status, Long authorId) {
        log.info("Listando publicaciones - Página: {}, Tamaño: {}, Status: {}, AuthorId: {}", 
                page, size, status, authorId);

        Pageable pageable = PageRequest.of(page, size);
        Page<PublicationImpl> publicationsPage;

        if (status != null && !status.isBlank()) {
            try {
                Publication.PublicationStatus pubStatus = 
                        Publication.PublicationStatus.valueOf(status.toUpperCase());
                publicationsPage = publicationRepository.findByStatus(pubStatus, pageable);
            } catch (IllegalArgumentException e) {
                log.warn("Estado inválido: {}", status);
                throw new InvalidPublicationException("Estado no válido: " + status);
            }
        } else if (authorId != null && authorId > 0) {
            publicationsPage = publicationRepository.findByAuthorId(authorId, pageable);
        } else {
            publicationsPage = publicationRepository.findAll(pageable);
        }

        return publicationsPage.map(this::mapToResponseDTO);
    }

    /**
     * Cambiar estado de publicación
     * Valida las transiciones permitidas
     */
    public PublicationResponseDTO updatePublicationStatus(Long id, 
            PublicationStatusUpdateDTO statusUpdate) {
        log.info("Actualizando estado de publicación {} a {}", id, statusUpdate.getStatus());

        PublicationImpl publication = publicationRepository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException("Publicación con ID " + id + 
                        " no encontrada"));

        // Validar nuevo estado
        Publication.PublicationStatus newStatus;
        try {
            newStatus = Publication.PublicationStatus.valueOf(statusUpdate.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Estado inválido: {}", statusUpdate.getStatus());
            throw new InvalidPublicationException("Estado no válido: " + statusUpdate.getStatus());
        }

        // Validar transición de estado permitida
        if (!publication.canTransitionTo(newStatus)) {
            log.warn("Transición de estado no permitida: {} -> {}", 
                    publication.getStatus(), newStatus);
            throw new InvalidStateTransitionException(
                    "No se puede cambiar de " + publication.getStatus() + " a " + newStatus);
        }

        // Actualizar estado
        publication.setStatus(newStatus);
        if (newStatus == Publication.PublicationStatus.IN_REVIEW) {
            publication.incrementReviewCount();
        }

        PublicationImpl updatedPublication = publicationRepository.save(publication);
        log.info("Publicación {} actualizada a estado {}", id, newStatus);

        return mapToResponseDTO(updatedPublication);
    }

    /**
     * Mapear PublicationImpl a ResponseDTO
     */
    private PublicationResponseDTO mapToResponseDTO(PublicationImpl publication) {
        return new PublicationResponseDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getDescription(),
                publication.getAuthorId(),
                publication.getStatus().toString(),
                publication.getCategory(),
                publication.getPublicationType(),
                publication.getCreatedAt(),
                publication.getUpdatedAt()
        );
    }

    /**
     * Mapear PublicationImpl a EnrichedDTO con datos del autor
     */
    private PublicationEnrichedDTO mapToEnrichedDTO(PublicationImpl publication,
            PublicationEnrichedDTO.AuthorDTO authorData) {
        return new PublicationEnrichedDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getDescription(),
                publication.getAuthorId(),
                authorData,
                publication.getStatus().toString(),
                publication.getCategory(),
                publication.getPublicationType(),
                publication.getCreatedAt(),
                publication.getUpdatedAt()
        );
    }
}
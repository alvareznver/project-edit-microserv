package com.editorial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Clase derivada PublicationImpl - Implementación concreta de Publication
 * Representa una publicación literaria con validaciones específicas
 */
@Entity
@Table(name = "publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicationImpl extends Publication {

    @Column(name = "publication_type", nullable = false)
    private String publicationType = "LITERARY_WORK";

    @Column(name = "review_count", nullable = false)
    private int reviewCount = 0;

    /**
     * Constructor con parámetros principales
     */
    public PublicationImpl(String title, String description, String content, Long authorId, String category) {
        super();
        this.setTitle(title);
        this.setDescription(description);
        this.setContent(content);
        this.setAuthorId(authorId);
        this.setCategory(category);
        this.publicationType = "LITERARY_WORK";
        this.reviewCount = 0;
    }

    /**
     * Validar transiciones de estado permitidas
     * Máquina de estados: DRAFT → IN_REVIEW → {APPROVED → PUBLISHED | REJECTED}
     */
    @Override
    public boolean canTransitionTo(PublicationStatus newStatus) {
        PublicationStatus currentStatus = this.getStatus();

        return switch (currentStatus) {
            case DRAFT -> newStatus == PublicationStatus.IN_REVIEW;
            case IN_REVIEW -> newStatus == PublicationStatus.APPROVED 
                           || newStatus == PublicationStatus.REJECTED
                           || newStatus == PublicationStatus.DRAFT; // Permitir volver a DRAFT para cambios
            case APPROVED -> newStatus == PublicationStatus.PUBLISHED 
                          || newStatus == PublicationStatus.REJECTED;
            case PUBLISHED, REJECTED -> false; // Estados finales
        };
    }

    /**
     * Obtener tipo de publicación
     */
    @Override
    public String getPublicationType() {
        return this.publicationType;
    }

    /**
     * Incrementar contador de revisiones
     */
    public void incrementReviewCount() {
        this.reviewCount++;
    }

    /**
     * Validar si publicación cumple requisitos para publicar
     */
    public boolean isReadyToPublish() {
        return this.getStatus() == PublicationStatus.APPROVED
                && this.getTitle() != null && !this.getTitle().isBlank()
                && this.getContent() != null && !this.getContent().isBlank()
                && this.getAuthorId() != null;
    }
}
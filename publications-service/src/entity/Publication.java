package com.editorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase abstracta Publication - Base para todas las entidades de publicación
 * Estados editoriales: DRAFT, IN_REVIEW, APPROVED, PUBLISHED, REJECTED
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicationStatus status = PublicationStatus.DRAFT;

    @Column(length = 100)
    private String category;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = PublicationStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Validar transición de estado
     */
    public abstract boolean canTransitionTo(PublicationStatus newStatus);

    /**
     * Obtener tipo de publicación
     */
    public abstract String getPublicationType();

    /**
     * Enum para estados editoriales
     */
    public enum PublicationStatus {
        DRAFT("Borrador"),
        IN_REVIEW("En revisión"),
        APPROVED("Aprobado"),
        PUBLISHED("Publicado"),
        REJECTED("Rechazado");

        private final String displayName;

        PublicationStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
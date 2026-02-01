package com.editorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase abstracta Author - Base para todas las entidades de autor
 * Aplicación de principios SOLID:
 * - SRP: Responsabilidad única de definir estructura de autor
 * - LSP: Las subclases pueden extender sin romper el contrato
 * - OCP: Abierta a extensión, cerrada a modificación
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String country;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Método abstracto para validación específica del tipo de autor
     * Las subclases deben implementar su propia lógica de validación
     */
    public abstract boolean isValidForType();

    /**
     * Método abstracto para obtener el tipo de autor
     */
    public abstract String getAuthorType();
}
package com.editorial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase derivada AuthorImpl - Implementación concreta de Author
 * Representa un autor independiente con validaciones específicas
 * 
 * Aplicación de principios SOLID:
 * - LSP: Puede reemplazar Author en cualquier lugar sin romper funcionalidad
 * - SRP: Responsabilidad única de gestionar autores independientes
 */
@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthorImpl extends Author {

    @Column(name = "author_type", nullable = false)
    private String authorType = "INDEPENDENT";

    /**
     * Constructor con parámetros principales
     */
    public AuthorImpl(String name, String email, String bio, String country) {
        super();
        this.setName(name);
        this.setEmail(email);
        this.setBio(bio);
        this.setCountry(country);
        this.authorType = "INDEPENDENT";
    }

    /**
     * Implementación de validación específica para autor independiente
     * Requiere: nombre, email y país
     */
    @Override
    public boolean isValidForType() {
        return this.getName() != null && !this.getName().isBlank()
                && this.getEmail() != null && !this.getEmail().isBlank()
                && this.getCountry() != null && !this.getCountry().isBlank();
    }

    /**
     * Retorna el tipo de autor
     */
    @Override
    public String getAuthorType() {
        return this.authorType;
    }

    /**
     * Método personalizado para formatear información del autor
     */
    public String getDisplayName() {
        return String.format("%s (%s)", this.getName(), this.getCountry());
    }
}
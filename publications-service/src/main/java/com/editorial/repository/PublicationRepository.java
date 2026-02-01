package com.editorial.repository;

import com.editorial.entity.Publication;
import com.editorial.entity.PublicationImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PublicationRepository - Patrón Repository para acceso a datos
 * Responsabilidad única: consultas a base de datos
 */
@Repository
public interface PublicationRepository extends JpaRepository<PublicationImpl, Long> {

    /**
     * Buscar publicaciones por autor
     * @param authorId ID del autor
     * @param pageable Configuración de paginación
     * @return Página de publicaciones
     */
    Page<PublicationImpl> findByAuthorId(Long authorId, Pageable pageable);

    /**
     * Buscar publicaciones por estado
     * @param status Estado editorial
     * @param pageable Configuración de paginación
     * @return Página de publicaciones
     */
    Page<PublicationImpl> findByStatus(Publication.PublicationStatus status, Pageable pageable);

    /**
     * Buscar por título (búsqueda parcial)
     * @param title Parte del título
     * @param pageable Configuración de paginación
     * @return Página de publicaciones
     */
    Page<PublicationImpl> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
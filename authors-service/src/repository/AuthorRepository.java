package com.editorial.repository;

import com.editorial.entity.AuthorImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AuthorRepository - Patrón Repository
 * Aplicación de principios SOLID:
 * - DIP: Abstracción de acceso a datos
 * - SRP: Responsabilidad única de consultas a BD
 * 
 * Spring Data JPA genera automáticamente las implementaciones
 */
@Repository
public interface AuthorRepository extends JpaRepository<AuthorImpl, Long> {

    /**
     * Buscar autor por email
     * @param email Email del autor
     * @return Optional con el autor si existe
     */
    Optional<AuthorImpl> findByEmail(String email);

    /**
     * Buscar autores por país
     * @param country País del autor
     * @param pageable Configuración de paginación
     * @return Página de autores
     */
    Page<AuthorImpl> findByCountry(String country, Pageable pageable);

    /**
     * Buscar si existe autor por ID
     * @param id ID del autor
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);
}
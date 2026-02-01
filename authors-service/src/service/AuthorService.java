package com.editorial.service;

import com.editorial.dto.AuthorExistsDTO;
import com.editorial.dto.AuthorRequestDTO;
import com.editorial.dto.AuthorResponseDTO;
import com.editorial.entity.AuthorImpl;
import com.editorial.exception.AuthorAlreadyExistsException;
import com.editorial.exception.AuthorNotFoundException;
import com.editorial.exception.InvalidAuthorException;
import com.editorial.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthorService - Servicio de Autores
 * Aplicación de patrones:
 * - Facade: Orquesta llamadas a repository
 * - SRP: Responsabilidad única de lógica de autor
 * - DIP: Depende de abstracciones (AuthorRepository)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Crear un nuevo autor
     * @param requestDTO Datos del autor a crear
     * @return AuthorResponseDTO con autor creado
     * @throws AuthorAlreadyExistsException si email ya existe
     * @throws InvalidAuthorException si datos son inválidos
     */
    public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
        log.info("Creando nuevo autor: {}", requestDTO.getEmail());

        // Validar que no exista autor con mismo email
        if (authorRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            log.warn("Intento de crear autor con email duplicado: {}", requestDTO.getEmail());
            throw new AuthorAlreadyExistsException("Autor con email " + requestDTO.getEmail() + " ya existe");
        }

        // Crear instancia de Author (usando derivada AuthorImpl)
        AuthorImpl author = new AuthorImpl(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getBio(),
                requestDTO.getCountry()
        );

        // Validar datos específicos del tipo
        if (!author.isValidForType()) {
            log.warn("Validación fallida para autor: {}", requestDTO.getEmail());
            throw new InvalidAuthorException("Datos incompletos para crear autor: nombre, email y país son requeridos");
        }

        // Persistir en BD
        AuthorImpl savedAuthor = authorRepository.save(author);
        log.info("Autor creado exitosamente con ID: {}", savedAuthor.getId());

        return mapToResponseDTO(savedAuthor);
    }

    /**
     * Obtener autor por ID
     * @param id ID del autor
     * @return AuthorResponseDTO
     * @throws AuthorNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public AuthorResponseDTO getAuthorById(Long id) {
        log.info("Obteniendo autor con ID: {}", id);

        AuthorImpl author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Autor no encontrado con ID: {}", id);
                    return new AuthorNotFoundException("Autor con ID " + id + " no encontrado");
                });

        return mapToResponseDTO(author);
    }

    /**
     * Listar autores con paginación
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @return Página de AuthorResponseDTO
     */
    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> listAuthors(int page, int size) {
        log.info("Listando autores - Página: {}, Tamaño: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorImpl> authorsPage = authorRepository.findAll(pageable);

        return authorsPage.map(this::mapToResponseDTO);
    }

    /**
     * Verificar si autor existe
     * @param id ID del autor
     * @return AuthorExistsDTO con información de existencia
     */
    @Transactional(readOnly = true)
    public AuthorExistsDTO checkAuthorExists(Long id) {
        log.info("Verificando existencia de autor con ID: {}", id);

        if (authorRepository.existsById(id)) {
            AuthorImpl author = authorRepository.findById(id).get();
            return new AuthorExistsDTO(true, author.getId(), author.getName());
        }

        return new AuthorExistsDTO(false, null, null);
    }

    /**
     * Mapear entidad AuthorImpl a DTO de respuesta
     * @param author Entidad de autor
     * @return AuthorResponseDTO
     */
    private AuthorResponseDTO mapToResponseDTO(AuthorImpl author) {
        return new AuthorResponseDTO(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getBio(),
                author.getCountry(),
                author.getAuthorType(),
                author.getCreatedAt(),
                author.getUpdatedAt()
        );
    }
}
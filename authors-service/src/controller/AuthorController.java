package com.editorial.controller;

import com.editorial.dto.AuthorExistsDTO;
import com.editorial.dto.AuthorRequestDTO;
import com.editorial.dto.AuthorResponseDTO;
import com.editorial.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthorController - REST Controller para gestión de autores
 * Aplicación de principios SOLID:
 * - SRP: Responsabilidad única de manejar HTTP
 * - DIP: Depende de AuthorService (abstracción)
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    /**
     * POST /api/authors - Crear nuevo autor
     * @param requestDTO Datos del autor en JSON
     * @return ResponseEntity con autor creado
     */
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody AuthorRequestDTO requestDTO) {
        log.info("POST /api/authors - Crear autor: {}", requestDTO.getEmail());
        AuthorResponseDTO response = authorService.createAuthor(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/authors/{id} - Obtener autor por ID
     * @param id ID del autor
     * @return ResponseEntity con datos del autor
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        log.info("GET /api/authors/{} - Obtener autor", id);
        AuthorResponseDTO response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/authors - Listar autores con paginación
     * @param page Número de página (default: 0)
     * @param size Tamaño de página (default: 10)
     * @return ResponseEntity con página de autores
     */
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> listAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/authors - Listar autores (página: {}, tamaño: {})", page, size);
        Page<AuthorResponseDTO> response = authorService.listAuthors(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/authors/{id}/exists - Verificar si autor existe
     * Usado internamente por Publications Service
     * @param id ID del autor
     * @return ResponseEntity con información de existencia
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<AuthorExistsDTO> checkAuthorExists(@PathVariable Long id) {
        log.info("GET /api/authors/{}/exists - Verificar existencia", id);
        AuthorExistsDTO response = authorService.checkAuthorExists(id);
        return ResponseEntity.ok(response);
    }
}
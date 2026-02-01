# Editorial Digital — Microservicios de Autores y Publicaciones

Sistema de gestion editorial basado en arquitectura de microservicios,
construido con Spring Boot, React y Docker Compose.

---

## Arquitectura

| Componente            | Tecnologia                | Puerto |
|-----------------------|---------------------------|--------|
| Authors Service       | Spring Boot + PostgreSQL  | 8080   |
| Publications Service  | Spring Boot + MySQL       | 8081   |
| Frontend              | React 18 + Material UI    | 3000   |
| db-authors            | PostgreSQL 15             | 5432   |
| db-publications       | MySQL 8.0                 | 3306   |

### Comunicacion entre servicios

Publications Service valida la existencia del autor llamando a Authors Service
mediante HTTP REST sincrono antes de crear una publicacion.

### Estados editoriales

```
DRAFT  →  IN_REVIEW  →  APPROVED  →  PUBLISHED
                     ↘  REJECTED
                     ↘  DRAFT  (retrabajo)
```

---

## Despliegue

### Requisitos

- Docker Desktop instalado y en ejecucion
- Git

### Pasos

```bash
git clone https://github.com/alvareznver/project-edit-microserv.git
cd project-edit-microserv
docker compose up --build
```

Esperar aprox. 40-60 segundos a que los healthchecks pasen.

### URLs

| Recurso                  | URL                                        |
|--------------------------|--------------------------------------------|
| Frontend                 | http://localhost:3000                       |
| Authors API              | http://localhost:8080/api/authors           |
| Authors Swagger          | http://localhost:8080/swagger-ui            |
| Publications API         | http://localhost:8081/api/publications      |
| Publications Swagger     | http://localhost:8081/swagger-ui            |

---

## Endpoints

### Authors Service

| Metodo | Endpoint                  | Descripcion                     |
|--------|---------------------------|---------------------------------|
| POST   | /api/authors              | Crear autor                     |
| GET    | /api/authors              | Listar autores (paginado)       |
| GET    | /api/authors/{id}         | Obtener autor por ID            |
| GET    | /api/authors/{id}/exists  | Verificar existencia            |

### Publications Service

| Metodo | Endpoint                       | Descripcion                          |
|--------|--------------------------------|--------------------------------------|
| POST   | /api/publications              | Crear publicacion (valida autor)     |
| GET    | /api/publications              | Listar (filtros opcionales)          |
| GET    | /api/publications/{id}         | Obtener con datos del autor          |
| PATCH  | /api/publications/{id}/status  | Cambiar estado editorial            |

---

## Variables de entorno (.env)

| Variable                   | Valor por defecto |
|----------------------------|-------------------|
| DB_AUTHORS_NAME            | authors_db        |
| DB_AUTHORS_USER            | admin             |
| DB_AUTHORS_PASSWORD        | admin123          |
| DB_PUBLICATIONS_NAME       | publications_db   |
| DB_PUBLICATIONS_USER       | admin             |
| DB_PUBLICATIONS_PASSWORD   | admin123          |
| AUTHORS_SERVICE_PORT       | 8080              |
| PUBLICATIONS_SERVICE_PORT  | 8081              |
| FRONTEND_PORT              | 3000              |

---

## Patrones de diseno

- **Repository Pattern** — Spring Data JPA desacopla el acceso a datos.
- **Adapter Pattern** — AuthorServiceClient adapta llamadas HTTP entre microservicios.
- **Strategy Pattern** — canTransitionTo() valida transiciones de estado editorial.

## Principios SOLID

- **SRP** — Controller, Service y Repository con responsabilidades separadas.
- **OCP** — Clases abstractas (Author, Publication) extensibles sin modificar codigo existente.
- **LSP** — Implementaciones concretas sustituyen validamente a sus clases base.
- **DIP** — Servicios dependen de interfaces Repository, no de implementaciones.

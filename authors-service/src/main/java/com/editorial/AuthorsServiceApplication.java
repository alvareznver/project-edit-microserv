package com.editorial;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * AuthorsServiceApplication - Punto de entrada de Authors Service
 * Configuración de Spring Boot y OpenAPI (Swagger)
 */
@SpringBootApplication
public class AuthorsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorsServiceApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authors Service API")
                        .version("1.0.0")
                        .description("Microservicio para gestión de autores en plataforma editorial"));
    }
}
package com.editorial;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * PublicationsServiceApplication - Punto de entrada de Publications Service
 */
@SpringBootApplication
public class PublicationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicationsServiceApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Publications Service API")
                        .version("1.0.0")
                        .description("Microservicio para gestión de publicaciones y estados editoriales"));
    }
}
package com.bookstudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "BookStudio API",
        version = "1.0.0",
        description = "REST API for a web library management platform."
    )
)
public class BookstudioApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookstudioApplication.class, args);
    }
}

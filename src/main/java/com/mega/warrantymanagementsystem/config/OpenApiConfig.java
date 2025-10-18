package com.mega.warrantymanagementsystem.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server ngrokServer = new Server()
                .url("https://balmily-superbenevolent-rubi.ngrok-free.dev")
                .description("Public via Ngrok");

        return new OpenAPI()
                .info(new Info()
                        .title("Warranty Management System API")
                        .version("1.0.0")
                        .description("API documentation for Warranty Management System"))
                .servers(List.of(ngrokServer));
    }
}

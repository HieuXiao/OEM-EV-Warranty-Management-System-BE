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
        Server railwayServer = new Server()
                .url("https://oem-ev-warranty-management-system-be-production.up.railway.app")
                .description("Public via Railway");

        return new OpenAPI()
                .info(new Info()
                        .title("Warranty Management System API")
                        .version("1.0.0")
                        .description("API documentation for Warranty Management System"))
                .servers(List.of(railwayServer));
    }
}
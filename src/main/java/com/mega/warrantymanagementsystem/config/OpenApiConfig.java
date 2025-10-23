//package com.mega.warrantymanagementsystem.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.util.List;
//
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // Lấy base URL tự động từ request hiện tại (http/https + domain + port)
//        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//
//        Server dynamicServer = new Server()
//                .url(baseUrl)
//                .description("Dynamic server URL (" + baseUrl + ")");
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Warranty Management System API")
//                        .version("1.0.0")
//                        .description("API documentation for Warranty Management System"))
//                .servers(List.of(dynamicServer));
//    }
//}

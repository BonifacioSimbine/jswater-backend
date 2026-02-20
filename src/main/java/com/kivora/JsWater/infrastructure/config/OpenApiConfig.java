package com.kivora.JsWater.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jsWaterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JsWater API")
                        .version("v1.0.0")
                        .description("""
                        Sistema de Gestão de Água.
                        
                        Funcionalidades:
                        - Gestão de Clientes
                        - Registo de Contadores
                        - Leitura de Consumo
                        - Geração de Faturas
                        """)
                        .contact(new Contact()
                                .name("Kivora Tech")
                                .email("support@kivora.tech")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                )
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Ambiente Local"));
    }
}

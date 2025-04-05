package com.wedding.backend.wedding_app.config.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final WeddingApiProperties apiProperties;

    @Bean
    public OpenAPI weddingAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiProperties.getTitle())
                        .description(apiProperties.getDescription())
                        .version(apiProperties.getVersion())
                        .contact(new Contact()
                                    .name(apiProperties.getContact().getName())
                                    .email(apiProperties.getContact().getEmail())))
                .servers(List.of(
                            new Server()
                                    .url(apiProperties.getServer().getUrl())
                                    .description(apiProperties.getServer().getDescription())
                ));
    }
}

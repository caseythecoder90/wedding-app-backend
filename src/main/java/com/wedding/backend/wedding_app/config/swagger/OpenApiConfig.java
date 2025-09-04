package com.wedding.backend.wedding_app.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private final WeddingApiProperties apiProperties;
    
    public OpenApiConfig(WeddingApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public OpenAPI weddingAppOpenAPI() {
        final String securitySchemeName = "X-API-Key";
        
        return new OpenAPI()
                .info(new Info()
                        .title(apiProperties.getTitle())
                        .description(apiProperties.getDescription() + 
                                "\n\n**Authentication:** All endpoints require an X-API-Key header with a base64-encoded API key." +
                                "\n\nTo use Swagger UI: Click the 'Authorize' button and enter your base64-encoded API key.")
                        .version(apiProperties.getVersion())
                        .contact(new Contact()
                                    .name(apiProperties.getContact().getName())
                                    .email(apiProperties.getContact().getEmail())))
                .servers(List.of(
                            new Server()
                                    .url(apiProperties.getServer().getUrl())
                                    .description(apiProperties.getServer().getDescription())))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("API Key authentication. Enter your base64-encoded API key here.\n\n" +
                                                "Example: if your API key is 'wedding-secret-key-change-me', " +
                                                "encode it as base64: 'd2VkZGluZy1zZWNyZXQta2V5LWNoYW5nZS1tZQ=='\n\n" +
                                                "You can generate the base64 encoding with: echo -n 'your-api-key' | base64")));
    }
}

package com.wedding.backend.wedding_app.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("wedding.api")
public class WeddingApiProperties {

    private String title;
    private String description;
    private String version;
    private Contact contact;
    private Server server;

    @Data
    public static class Contact {
        private String name;
        private String email;
    }

    @Data
    public static class Server {
        private String url;
        private String description;
    }
}

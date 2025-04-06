package com.wedding.backend.wedding_app.model.reponse;

import lombok.Data;

import java.time.Instant;

/**
 * Response for Health Check endpoint.
 */
@Data
public class HealthCheckResponse {
    private String status;
    private String message;
    private String timestamp;

    private HealthCheckResponse(Builder builder) {
       this.message = builder.message;
       this.status = builder.status;
       this.timestamp = builder.timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String status;
        private String message;
        private String timestamp;

        public Builder status(String status) {
            this.status = status;
            this.timestamp = Instant.now().toString();
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public HealthCheckResponse build() {
            return new HealthCheckResponse(this);
        }

    }
}

package com.wedding.backend.wedding_app.model.reponse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response object for debug API endpoints
 */
@Schema(description = "Response object for debug API endpoints")
public class DebugResponse {

    @Schema(description = "Operation status (success or error)")
    private String status;

    @Schema(description = "Detailed response message")
    private String message;

    @Schema(description = "Timestamp of the operation")
    private String timestamp;

    @Schema(description = "Additional details or context about the operation")
    private String details;

    // Default constructor
    public DebugResponse() {
    }

    // Private constructor for builder
    private DebugResponse(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
        this.details = builder.details;
    }

    // Static method to get builder instance
    public static Builder builder() {
        return new Builder();
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Builder class
    public static class Builder {
        private String status;
        private String message;
        private String timestamp;
        private String details;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public DebugResponse build() {
            return new DebugResponse(this);
        }
    }
}
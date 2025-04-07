package com.wedding.backend.wedding_app.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ErrorDefinitionRequest {

    @NotBlank(message = "Error key is required")
    @Pattern(regexp = "^[A-Z_]+$", message = "Error key must be in uppercase with underscores")
    private String errorKey;

    @NotBlank(message = "Error code is required")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}$", message = "Error code must be in format XXX.XXX")
    private String errorCode;

    @NotBlank(message = "Error reason is required")
    private String errorReason;

    @NotBlank(message = "Error message is required")
    private String errorMessage;

    private boolean retryable;
    
    // Default constructor
    public ErrorDefinitionRequest() {
    }
    
    // Constructor with all fields
    private ErrorDefinitionRequest(Builder builder) {
        this.errorKey = builder.errorKey;
        this.errorCode = builder.errorCode;
        this.errorReason = builder.errorReason;
        this.errorMessage = builder.errorMessage;
        this.retryable = builder.retryable;
    }
    
    // Getters and setters
    public String getErrorKey() {
        return errorKey;
    }
    
    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorReason() {
        return errorReason;
    }
    
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean isRetryable() {
        return retryable;
    }
    
    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String errorKey;
        private String errorCode;
        private String errorReason;
        private String errorMessage;
        private boolean retryable;
        
        public Builder errorKey(String errorKey) {
            this.errorKey = errorKey;
            return this;
        }
        
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public Builder errorReason(String errorReason) {
            this.errorReason = errorReason;
            return this;
        }
        
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public Builder retryable(boolean retryable) {
            this.retryable = retryable;
            return this;
        }
        
        public ErrorDefinitionRequest build() {
            return new ErrorDefinitionRequest(this);
        }
    }
    
    @Override
    public String toString() {
        return "ErrorDefinitionRequest{" +
                "errorKey='" + errorKey + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorReason='" + errorReason + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", retryable=" + retryable +
                '}';
    }
}
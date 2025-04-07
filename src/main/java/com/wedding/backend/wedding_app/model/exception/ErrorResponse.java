package com.wedding.backend.wedding_app.model.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private HttpStatus status;
    private String errorCode;
    private String errorMessage;
    private String errorReason;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private boolean retryable;
    private String errorId;
    private List<Detail> details;
    
    // Default constructor
    public ErrorResponse() {
    }
    
    // Constructor from builder
    private ErrorResponse(Builder builder) {
        this.status = builder.status;
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.errorReason = builder.errorReason;
        this.path = builder.path;
        this.timestamp = builder.timestamp;
        this.retryable = builder.retryable;
        this.errorId = builder.errorId;
        this.details = builder.details;
    }
    
    // Getters and Setters
    public HttpStatus getStatus() {
        return status;
    }
    
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getErrorReason() {
        return errorReason;
    }
    
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isRetryable() {
        return retryable;
    }
    
    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }
    
    public String getErrorId() {
        return errorId;
    }
    
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
    
    public List<Detail> getDetails() {
        return details;
    }
    
    public void setDetails(List<Detail> details) {
        this.details = details;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private HttpStatus status;
        private String errorCode;
        private String errorMessage;
        private String errorReason;
        private String path;
        private LocalDateTime timestamp;
        private boolean retryable;
        private String errorId;
        private List<Detail> details;
        
        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public Builder errorReason(String errorReason) {
            this.errorReason = errorReason;
            return this;
        }
        
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder retryable(boolean retryable) {
            this.retryable = retryable;
            return this;
        }
        
        public Builder errorId(String errorId) {
            this.errorId = errorId;
            return this;
        }
        
        public Builder details(List<Detail> details) {
            this.details = details;
            return this;
        }
        
        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}

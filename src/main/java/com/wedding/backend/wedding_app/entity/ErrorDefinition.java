package com.wedding.backend.wedding_app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "error_codes")
public class ErrorDefinition {

    @Id
    @Column(name = "uniq_err_key")
    private String errorKey;

    @Column(name = "err_cd")
    private String errorCode;

    @Column(name = "err_reason")
    private String errorReason;

    @Column(name = "err_msg")
    private String errorMessage;

    @Column(name = "retryable")
    private boolean retryable;
    
    // Constructors
    public ErrorDefinition() {
    }
    
    private ErrorDefinition(Builder builder) {
        this.errorKey = builder.errorKey;
        this.errorCode = builder.errorCode;
        this.errorReason = builder.errorReason;
        this.errorMessage = builder.errorMessage;
        this.retryable = builder.retryable;
    }
    
    // Getters and Setters
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
        
        public ErrorDefinition build() {
            return new ErrorDefinition(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ErrorDefinition that = (ErrorDefinition) o;
        
        return errorKey != null ? errorKey.equals(that.errorKey) : that.errorKey == null;
    }
    
    @Override
    public int hashCode() {
        return errorKey != null ? errorKey.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "ErrorDefinition{" +
                "errorKey='" + errorKey + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorReason='" + errorReason + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", retryable=" + retryable +
                '}';
    }
}

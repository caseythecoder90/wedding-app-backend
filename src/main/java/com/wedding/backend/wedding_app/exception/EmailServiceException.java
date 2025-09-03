package com.wedding.backend.wedding_app.exception;

/**
 * Custom exception for email service operations
 */
public class EmailServiceException extends RuntimeException {

    public EmailServiceException(String message) {
        super(message);
    }

    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmailServiceException templateNotFound(String templateName) {
        return new EmailServiceException("Email template not found: " + templateName);
    }

    public static EmailServiceException templateProcessingFailed(String templateName) {
        return new EmailServiceException("Email template processing failed: " + templateName);
    }

    public static EmailServiceException emailSendFailed(String recipient) {
        return new EmailServiceException("Failed to send email to: " + recipient);
    }

    public static EmailServiceException unexpectedError(String operation) {
        return new EmailServiceException("Unexpected error during email operation: " + operation);
    }
}
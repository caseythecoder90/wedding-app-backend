package com.wedding.backend.wedding_app.exception;

public class WeddingAppException extends RuntimeException {
    private final String errorKey;
    private Object details;
    
    // Getters
    public String getErrorKey() {
        return errorKey;
    }
    
    public Object getDetails() {
        return details;
    }
    
    public void setDetails(Object details) {
        this.details = details;
    }

    public WeddingAppException(String errorKey) {
        super("Error occurred: " + errorKey);
        this.errorKey = errorKey;
    }

    public WeddingAppException(String errorKey, Object details) {
        super("Error occurred: " + errorKey);
        this.errorKey = errorKey;
        this.details = details;
    }

    public WeddingAppException(String errorKey, Throwable cause) {
        super("Error occurred: " + errorKey, cause);
        this.errorKey = errorKey;
    }

    public WeddingAppException(String errorKey, Throwable cause, Object details) {
        super("Error occurred: " + errorKey, cause);
        this.errorKey = errorKey;
        this.details = details;
    }

    // Factory methods for common exceptions
    public static WeddingAppException guestNotFound(Long id) {
        return new WeddingAppException("GUEST_NOT_FOUND", id);
    }

    public static WeddingAppException guestNameNotFound(String firstName, String lastName) {
        return new WeddingAppException("GUEST_NAME_NOT_FOUND",
                String.format("Guest not found with name: %s %s", firstName, lastName));
    }

    public static WeddingAppException validationError(String field) {
        return new WeddingAppException("VALIDATION_ERROR", field);
    }

    public static WeddingAppException databaseError() {
        return new WeddingAppException("DATABASE_ERROR");
    }

    public static WeddingAppException rsvpNotFound(Long id) {
        return new WeddingAppException("RSVP_NOT_FOUND", id);
    }

    public static WeddingAppException duplicateRSVP(Long guestId) {
        return new WeddingAppException("RSVP_ALREADY_SUBMITTED", guestId);
    }

    public static WeddingAppException duplicateGuest(String firstName, String lastName) {
        return new WeddingAppException("GUEST_ALREADY_EXISTS",
                String.format("Guest already exists with name: %s %s", firstName, lastName));
    }


    public static WeddingAppException invalidParameter(String paramName) {
        return new WeddingAppException("INVALID_PARAMETER", paramName);
    }

    public static WeddingAppException missingParameter(String paramName) {
        return new WeddingAppException("MISSING_PARAMETER", paramName);
    }

    public static WeddingAppException internalError(String message) {
        return new WeddingAppException("INTERNAL_SERVER_ERROR", message);
    }
}
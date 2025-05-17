package com.wedding.backend.wedding_app.util;

/**
 * Constants for error keys used throughout the wedding application
 */
public final class WeddingErrorConstants {

    private WeddingErrorConstants() {
        // Private constructor to prevent instantiation
    }

    // Not Found errors (404)
    public static final String GUEST_NOT_FOUND = "GUEST_NOT_FOUND";
    public static final String GUEST_NAME_NOT_FOUND = "GUEST_NAME_NOT_FOUND";
    public static final String RSVP_NOT_FOUND = "RSVP_NOT_FOUND";
    public static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";
    public static final String ERROR_KEY_NOT_FOUND = "ERROR_KEY_NOT_FOUND";
    
    // Conflict errors (409)
    public static final String GUEST_ALREADY_EXISTS = "GUEST_ALREADY_EXISTS";
    public static final String RSVP_ALREADY_SUBMITTED = "RSVP_ALREADY_SUBMITTED";
    public static final String ERROR_KEY_ALREADY_EXISTS = "ERROR_KEY_ALREADY_EXISTS";

    // Bad Request errors (400)
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String MISSING_PARAMETER = "MISSING_PARAMETER";
    
    // Unauthorized (401)
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    
    // Server errors (500)
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    
    // Invitation specific errors
    public static final String INVALID_INVITATION_CODE = "INVALID_INVITATION_CODE";
    public static final String EXPIRED_INVITATION_CODE = "EXPIRED_INVITATION_CODE";
    public static final String USED_INVITATION_CODE = "USED_INVITATION_CODE";
    public static final String INVITATION_CODE_CREATION_ERROR = "INVITATION_CODE_CREATION_ERROR";
}
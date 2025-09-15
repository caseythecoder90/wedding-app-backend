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

    public static WeddingAppException donationNotFound(Long id) {
        return new WeddingAppException("DONATION_NOT_FOUND", id);
    }

    public static WeddingAppException entityNotFound(String entityName, Long id) {
        return new WeddingAppException("ENTITY_NOT_FOUND", 
                String.format("%s not found with id: %d", entityName, id));
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
    
    // Invitation code related exceptions
    public static WeddingAppException invalidInvitationCode(String code) {
        return new WeddingAppException("INVALID_INVITATION_CODE", 
                String.format("Invalid invitation code: %s", code));
    }
    
    public static WeddingAppException expiredInvitationCode(String code) {
        return new WeddingAppException("EXPIRED_INVITATION_CODE", 
                String.format("Invitation code has expired: %s", code));
    }
    
    public static WeddingAppException usedInvitationCode(String code) {
        return new WeddingAppException("USED_INVITATION_CODE", 
                String.format("Invitation code has already been used: %s", code));
    }
    
    public static WeddingAppException invitationCodeCreationError(Long guestId) {
        return new WeddingAppException("INVITATION_CODE_CREATION_ERROR", 
                String.format("Error creating invitation code for guest: %d", guestId));
    }
    
    // Family-related exceptions
    public static WeddingAppException familyGroupNotFound(Long id) {
        return new WeddingAppException("FAMILY_GROUP_NOT_FOUND", id);
    }
    
    public static WeddingAppException familyMemberNotFound(Long id) {
        return new WeddingAppException("FAMILY_MEMBER_NOT_FOUND", id);
    }
    
    public static WeddingAppException guestAlreadyPrimaryContact(Long guestId) {
        return new WeddingAppException("GUEST_ALREADY_PRIMARY_CONTACT", 
                String.format("Guest %d is already a primary contact for another family group", guestId));
    }
    
    public static WeddingAppException familyGroupFull(Long familyGroupId, int maxAttendees) {
        return new WeddingAppException("FAMILY_GROUP_FULL", 
                String.format("Family group %d is full (max attendees: %d)", familyGroupId, maxAttendees));
    }
    
    public static WeddingAppException invalidFamilyGroupSize(int size) {
        return new WeddingAppException("INVALID_FAMILY_GROUP_SIZE", 
                String.format("Invalid family group size: %d", size));
    }
    
    // Email-related exceptions
    public static WeddingAppException emailSendError(String operation, String details) {
        return new WeddingAppException("EMAIL_SEND_ERROR", 
                String.format("Failed to send %s email: %s", operation, details));
    }
    
    public static WeddingAppException emailSendError(String operation, Throwable cause) {
        return new WeddingAppException("EMAIL_SEND_ERROR", cause, 
                String.format("Failed to send %s email", operation));
    }
    
    public static WeddingAppException emailTemplateError(String templateName, Throwable cause) {
        return new WeddingAppException("EMAIL_TEMPLATE_ERROR", cause, 
                String.format("Error processing email template: %s", templateName));
    }
}
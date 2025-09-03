package com.wedding.backend.wedding_app.util;

import org.springframework.stereotype.Component;

@Component
public class WeddingServiceConstants {

    public static final String SUCCESS = "SUCCESS";
    public static final String ALIVE = "I am alive.";
    public static final String UPDATED = "updated";
    public static final String CREATED = "created";
    public static final String SPACE = " ";

    // RSVP and email template paths
    public static final String ATTENDING_RSVP_CONFIRMATION = "email/attending.ftlh";
    public static final String NOT_ATTENDING_RSVP_CONFIRMATION = "email/not-attending.ftlh";
    public static final String ADMIN_RSVP_NOTIFICATION = "email/admin-rsvp-notif.ftlh";

    // Email model field keys
    public static final String EMAIL_FIELD_FIRST_NAME = "firstName";
    public static final String EMAIL_FIELD_LAST_NAME = "lastName";
    public static final String EMAIL_FIELD_ATTENDING = "attending";
    public static final String EMAIL_FIELD_BRINGING_PLUS_ONE = "bringingPlusOne";
    public static final String EMAIL_FIELD_PLUS_ONE_NAME = "plusOneName";
    public static final String EMAIL_FIELD_DIETARY_RESTRICTIONS = "dietaryRestrictions";
    public static final String EMAIL_FIELD_RSVP_ID = "rsvpId";
    public static final String EMAIL_FIELD_GUEST_EMAIL = "guestEmail";
    public static final String EMAIL_FIELD_SUBMISSION_DATE = "submissionDate";

    // Family RSVP email fields
    public static final String EMAIL_FIELD_IS_FAMILY_RSVP = "isFamilyRsvp";
    public static final String EMAIL_FIELD_FAMILY_GROUP_NAME = "familyGroupName";
    public static final String EMAIL_FIELD_FAMILY_MEMBERS = "familyMembers";
    public static final String EMAIL_FIELD_FAMILY_ATTENDING_COUNT = "familyAttendingCount";
    public static final String EMAIL_FIELD_FAMILY_TOTAL_COUNT = "familyTotalCount";

    // Email service constants
    public static final String EMAIL_CHARSET = "UTF-8";
    public static final String DATE_FORMAT_PATTERN = "MMMM d, yyyy 'at' h:mm a";

    // Family member email model field keys
    public static final String FAMILY_MEMBER_FIRST_NAME = "firstName";
    public static final String FAMILY_MEMBER_LAST_NAME = "lastName";
    public static final String FAMILY_MEMBER_AGE_GROUP = "ageGroup";
    public static final String FAMILY_MEMBER_IS_ATTENDING = "isAttending";
    public static final String FAMILY_MEMBER_DIETARY_RESTRICTIONS = "dietaryRestrictions";

    // Email service log messages
    public static final String EMAIL_LOG_STARTED_RSVP = "STARTED - Sending RSVP confirmation email to: {}";
    public static final String EMAIL_LOG_COMPLETED_RSVP = "COMPLETED - RSVP confirmation email sent successfully to: {}";
    public static final String EMAIL_LOG_STARTED_ADMIN = "STARTED - Sending admin notification with RSVP summary";
    public static final String EMAIL_LOG_COMPLETED_ADMIN = "COMPLETED - Admin notification sent successfully";
    public static final String EMAIL_LOG_STARTED_DONATION = "STARTED - Sending donation confirmation email to: {}";
    public static final String EMAIL_LOG_COMPLETED_DONATION = "COMPLETED - Donation confirmation email sent successfully to: {}";
    public static final String EMAIL_LOG_STARTED_THANK_YOU = "STARTED - Sending donation thank you email to: {}";
    public static final String EMAIL_LOG_COMPLETED_THANK_YOU = "COMPLETED - Thank you email sent successfully to: {}";
    public static final String EMAIL_LOG_INVALID_EMAIL = "Cannot send email - guest email is invalid";
    public static final String EMAIL_LOG_INVALID_DONATION_EMAIL = "Cannot send donation confirmation email - donor email is invalid";
    public static final String EMAIL_LOG_INVALID_THANK_YOU_EMAIL = "Cannot send thank you email - donor email is invalid";
    public static final String EMAIL_LOG_UNCONFIRMED_DONATION = "Cannot send thank you email - donation is not confirmed";
    public static final String EMAIL_LOG_CREATING_EMAIL = "Creating and sending email to: {}";
    public static final String EMAIL_LOG_EMAIL_SENT = "Email sent successfully to: {}";
    public static final String EMAIL_LOG_FAMILY_INFO = "Adding family information to email model for group: {}";
    public static final String EMAIL_LOG_ERROR_RSVP_SUMMARY = "Error building RSVP summary for admin notification";
    public static final String EMAIL_LOG_ERROR_RSVP_EMAIL = "Exception while sending RSVP email: ";
    public static final String EMAIL_LOG_ERROR_ADMIN_EMAIL = "Exception while sending admin notification: ";
    public static final String EMAIL_LOG_ERROR_EMAIL_OPERATION = "Error during email operation: {}";

    // Admin notification model fields
    public static final String ADMIN_FIELD_TOTAL_RSVPS = "totalRsvps";
    public static final String ADMIN_FIELD_TOTAL_ATTENDING = "totalAttending";
    public static final String ADMIN_FIELD_TOTAL_NOT_ATTENDING = "totalNotAttending";
    public static final String ADMIN_FIELD_TOTAL_GUESTS = "totalGuests";
    public static final String ADMIN_FIELD_ATTENDING_RSVPS = "attendingRsvps";
    public static final String ADMIN_FIELD_NOT_ATTENDING_RSVPS = "notAttendingRsvps";
    public static final String ADMIN_FIELD_LAST_UPDATED = "lastUpdated";
    public static final String ADMIN_FIELD_SUMMARY_ERROR = "summaryError";
    public static final String ADMIN_FIELD_ERROR_MESSAGE = "errorMessage";
    public static final String ADMIN_FIELD_ATTENDING_FAMILY_MEMBERS = "attendingFamilyMembers";

    // Email subjects
    public static final String ATTENDING_EMAIL_SUBJECT = "Wedding RSVP Confirmation - We're excited to see you!";
    public static final String NOT_ATTENDING_EMAIL_SUBJECT = "Wedding RSVP Confirmation - Thank you for your response!";
    public static final String ADMIN_NOTIFICATION_SUBJECT = "New Wedding RSVP Submission";

    // QR Code & invitation code related
    public static final String BASE_URL = "https://wedding-app-frontend.vercel.app/rsvp?code=";
    public static final int QR_CODE_SIZE = 300;
    public static final String PNG = "PNG";
    public static final String PNG_EXTENSION = ".png";
    public static final String CODE_PREFIX = "WED";
    public static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int CODE_LENGTH = 6;


    // Registry specific errors
    public static final String REGISTRY_NOT_FOUND = "REGISTRY_NOT_FOUND";
    public static final String REGISTRY_INACTIVE = "REGISTRY_INACTIVE";
    public static final String DONATION_NOT_FOUND = "DONATION_NOT_FOUND";
    public static final String INVALID_DONATION_AMOUNT = "INVALID_DONATION_AMOUNT";
    public static final String DUPLICATE_DONATION = "DUPLICATE_DONATION";
    public static final String DONATION_ALREADY_CONFIRMED = "DONATION_ALREADY_CONFIRMED";
    public static final String THANK_YOU_ALREADY_SENT = "THANK_YOU_ALREADY_SENT";
    public static final String DONATION_NOT_CONFIRMED = "DONATION_NOT_CONFIRMED";
    public static final String TEMPLATE_NOT_FOUND = "TEMPLATE_NOT_FOUND";
    public static final String DEFAULT_TEMPLATE_REQUIRED = "DEFAULT_TEMPLATE_REQUIRED";

    // Email template paths (add to WeddingServiceConstants.java)
    public static final String DONATION_CONFIRMATION_TEMPLATE = "email/donation-confirmation.ftlh";
    public static final String DONATION_THANK_YOU_TEMPLATE = "email/donation-thank-you.ftlh";

    // Email subjects (add to WeddingServiceConstants.java)
    public static final String DONATION_CONFIRMATION_SUBJECT = "Thank you for your honeymoon contribution!";
    public static final String DONATION_THANK_YOU_SUBJECT = "Thank you for your generous honeymoon gift! 💕";

    // Email field constants for donation templates (add to WeddingServiceConstants.java)
    public static final String EMAIL_FIELD_DONOR_NAME = "donorName";
    public static final String EMAIL_FIELD_DONOR_EMAIL = "donorEmail";
    public static final String EMAIL_FIELD_DONOR_PHONE = "donorPhone";
    public static final String EMAIL_FIELD_DONATION_AMOUNT = "donationAmount";
    public static final String EMAIL_FIELD_PAYMENT_METHOD = "paymentMethod";
    public static final String EMAIL_FIELD_PAYMENT_REFERENCE = "paymentReference";
    public static final String EMAIL_FIELD_DONATION_MESSAGE = "donationMessage";
    public static final String EMAIL_FIELD_DONATION_ID = "donationId";
    public static final String EMAIL_FIELD_DONATION_DATE = "donationDate";
    public static final String EMAIL_FIELD_CONFIRMED_DATE = "confirmedDate";
    public static final String EMAIL_FIELD_DONATION_STATUS = "donationStatus";
    public static final String EMAIL_FIELD_HAS_PAYMENT_REFERENCE = "hasPaymentReference";
    public static final String EMAIL_FIELD_HAS_DONATION_MESSAGE = "hasDonationMessage";
    public static final String EMAIL_FIELD_HAS_PHONE = "hasPhone";

}

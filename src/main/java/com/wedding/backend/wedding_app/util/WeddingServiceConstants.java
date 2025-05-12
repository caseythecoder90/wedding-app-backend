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

}

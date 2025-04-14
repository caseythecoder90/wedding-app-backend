package com.wedding.backend.wedding_app.util;

import org.springframework.stereotype.Component;

@Component
public class WeddingServiceConstants {

    public static final String SUCCESS = "SUCCESS";
    public static final String ALIVE = "I am alive.";
    public static final String UPDATED = "updated";
    public static final String CREATED = "created";

    // RSVP and email setup
    public static final String ATTENDING_RSVP_CONFIRMATION = "email/attending.ftlh";
    public static final String NOT_ATTENDING_RSVP_CONFIRMATION = "email/not-attending.ftlh";

}

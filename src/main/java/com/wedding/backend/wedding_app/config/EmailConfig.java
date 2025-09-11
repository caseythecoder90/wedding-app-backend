package com.wedding.backend.wedding_app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for email functionality
 */
@Configuration
@ConfigurationProperties(prefix = "wedding.email")
@Data
public class EmailConfig {
    
    // Template paths - English (default)
    private String attendingTemplatePath;
    private String notAttendingTemplatePath;
    private String adminNotificationTemplatePath;
    
    // Template paths - Portuguese
    private String attendingTemplatePathPt;
    private String notAttendingTemplatePathPt;
    
    // Email subjects - English (default)
    private String attendingSubject;
    private String notAttendingSubject;
    private String adminNotificationSubject;
    
    // Email subjects - Portuguese
    private String attendingSubjectPt;
    private String notAttendingSubjectPt;
    
    // Default sender email
    private String senderEmail;
    private String adminEmail;
    
    // Feature flags
    private boolean sendAdminNotifications;

    private String donationConfirmationTemplatePath;
    private String donationThankYouTemplatePath;
    private String donationConfirmationSubject;
    private String donationThankYouSubject;
}
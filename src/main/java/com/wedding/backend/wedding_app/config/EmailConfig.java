package com.wedding.backend.wedding_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for email functionality
 */
@Configuration
@ConfigurationProperties(prefix = "wedding.email")
public class EmailConfig {
    
    // Template paths
    private String attendingTemplatePath;
    private String notAttendingTemplatePath;
    private String adminNotificationTemplatePath;
    
    // Email subjects
    private String attendingSubject;
    private String notAttendingSubject;
    private String adminNotificationSubject;
    
    // Default sender email
    private String senderEmail;
    private String adminEmail;
    
    // Feature flags
    private boolean sendAdminNotifications;

    private String donationConfirmationTemplatePath;
    private String donationThankYouTemplatePath;
    private String donationConfirmationSubject;
    private String donationThankYouSubject;

    // Add getters:
    public String getDonationConfirmationTemplatePath() {
        return donationConfirmationTemplatePath;
    }

    public void setDonationConfirmationTemplatePath(String donationConfirmationTemplatePath) {
        this.donationConfirmationTemplatePath = donationConfirmationTemplatePath;
    }

    public String getDonationThankYouTemplatePath() {
        return donationThankYouTemplatePath;
    }

    public void setDonationThankYouTemplatePath(String donationThankYouTemplatePath) {
        this.donationThankYouTemplatePath = donationThankYouTemplatePath;
    }

    public String getDonationConfirmationSubject() {
        return donationConfirmationSubject;
    }

    public void setDonationConfirmationSubject(String donationConfirmationSubject) {
        this.donationConfirmationSubject = donationConfirmationSubject;
    }

    public String getDonationThankYouSubject() {
        return donationThankYouSubject;
    }

    public void setDonationThankYouSubject(String donationThankYouSubject) {
        this.donationThankYouSubject = donationThankYouSubject;
    }
    
    // Getters and setters
    public String getAttendingTemplatePath() {
        return attendingTemplatePath;
    }
    
    public void setAttendingTemplatePath(String attendingTemplatePath) {
        this.attendingTemplatePath = attendingTemplatePath;
    }
    
    public String getNotAttendingTemplatePath() {
        return notAttendingTemplatePath;
    }
    
    public void setNotAttendingTemplatePath(String notAttendingTemplatePath) {
        this.notAttendingTemplatePath = notAttendingTemplatePath;
    }
    
    public String getAdminNotificationTemplatePath() {
        return adminNotificationTemplatePath;
    }
    
    public void setAdminNotificationTemplatePath(String adminNotificationTemplatePath) {
        this.adminNotificationTemplatePath = adminNotificationTemplatePath;
    }
    
    public String getAttendingSubject() {
        return attendingSubject;
    }
    
    public void setAttendingSubject(String attendingSubject) {
        this.attendingSubject = attendingSubject;
    }
    
    public String getNotAttendingSubject() {
        return notAttendingSubject;
    }
    
    public void setNotAttendingSubject(String notAttendingSubject) {
        this.notAttendingSubject = notAttendingSubject;
    }
    
    public String getAdminNotificationSubject() {
        return adminNotificationSubject;
    }
    
    public void setAdminNotificationSubject(String adminNotificationSubject) {
        this.adminNotificationSubject = adminNotificationSubject;
    }
    
    public String getSenderEmail() {
        return senderEmail;
    }
    
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    
    public String getAdminEmail() {
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
    
    public boolean isSendAdminNotifications() {
        return sendAdminNotifications;
    }
    
    public void setSendAdminNotifications(boolean sendAdminNotifications) {
        this.sendAdminNotifications = sendAdminNotifications;
    }
}
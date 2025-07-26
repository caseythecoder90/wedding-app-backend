package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.config.EmailConfig;
import com.wedding.backend.wedding_app.dto.RSVPSummaryDTO;
import com.wedding.backend.wedding_app.entity.DonationEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.*;

/**
 * Service for handling all email functionality in the application
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final EmailConfig emailConfig;
    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

    public EmailService(JavaMailSender mailSender, Configuration freemarkerConfig,
                        EmailConfig emailConfig) {
        this.mailSender = mailSender;
        this.freemarkerConfig = freemarkerConfig;
        this.emailConfig = emailConfig;
    }

    /**
     * Sends an RSVP confirmation email based on the RSVP status (attending or not attending)
     * @param rsvpEntity The RSVP entity containing all needed data
     * @param guestEntity The guest entity
     */
    public void sendRSVPConfirmationEmail(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        log.info("STARTED - Sending RSVP confirmation email to: {}", guestEntity.getEmail());

        // Validate email
        if (StringUtils.isBlank(guestEntity.getEmail())) {
            log.warn("Cannot send email - guest email is invalid");
            return;
        }

        boolean isAttending = rsvpEntity.getAttending();

        try {

            String templatePath = isAttending
                    ? emailConfig.getAttendingTemplatePath()
                    : emailConfig.getNotAttendingTemplatePath();

            String subject = isAttending
                    ? emailConfig.getAttendingSubject()
                    : emailConfig.getNotAttendingSubject();

            Map<String, Object> model = buildRsvpEmailModel(rsvpEntity, guestEntity);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(guestEntity.getEmail(), subject, htmlContent);
            
            log.info("COMPLETED - RSVP confirmation email sent successfully to: {}", guestEntity.getEmail());

        } catch (Exception e) {
            log.error("Exception while sending RSVP email: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Send notification to admin about new RSVP along with a summary of all RSVPs
     * @param rsvpEntity The RSVP entity that triggered the notification
     * @param guestEntity The guest entity that triggered the notification
     * @param rsvpSummary The summary of all RSVPs
     */
    public void sendAdminRsvpNotification(RSVPEntity rsvpEntity, GuestEntity guestEntity, RSVPSummaryDTO rsvpSummary) {
        log.info("STARTED - Sending admin notification with RSVP summary");

        try {
            String templatePath = emailConfig.getAdminNotificationTemplatePath();

            // Fallback to constant if config value is not set
            if (StringUtils.isBlank(templatePath)) {
                templatePath = ADMIN_RSVP_NOTIFICATION;
            }

            String subject = emailConfig.getAdminNotificationSubject();

            // Fallback to constant if config value is not set
            if (StringUtils.isBlank(subject)) {
                subject = ADMIN_NOTIFICATION_SUBJECT;
            }

            // Build the template model for both the triggering RSVP and the summary
            Map<String, Object> model = buildAdminNotificationModel(rsvpEntity, guestEntity, rsvpSummary);

            // Process the template and send the email
            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(emailConfig.getAdminEmail(), subject, htmlContent);
            
            log.info("COMPLETED - Admin notification sent successfully");

        } catch (Exception e) {
            log.error("Exception while sending admin notification: ", e);
            // Don't rethrow - admin notifications are non-critical
        }
    }

    /**
     * Build a comprehensive model for admin notifications, including a summary of all RSVPs
     * @param triggerRsvpEntity The RSVP entity that triggered the notification
     * @param triggerGuestEntity The guest entity that triggered the notification
     * @param rsvpSummary The summary data of all RSVPs
     * @return Map containing all template variables including RSVP summary data
     */
    private Map<String, Object> buildAdminNotificationModel(
            RSVPEntity triggerRsvpEntity,
            GuestEntity triggerGuestEntity,
            RSVPSummaryDTO rsvpSummary) {

        // First, add the triggering RSVP details
        Map<String, Object> model = buildRsvpEmailModel(triggerRsvpEntity, triggerGuestEntity);

        try {
            // Add summary data to the model
            model.put(ADMIN_FIELD_TOTAL_RSVPS, rsvpSummary.getTotalRsvps());
            model.put(ADMIN_FIELD_TOTAL_ATTENDING, rsvpSummary.getTotalAttending());
            model.put(ADMIN_FIELD_TOTAL_NOT_ATTENDING, rsvpSummary.getTotalNotAttending());
            model.put(ADMIN_FIELD_TOTAL_GUESTS, rsvpSummary.getTotalGuests());
            model.put(ADMIN_FIELD_ATTENDING_RSVPS, rsvpSummary.getAttendingRsvps());
            model.put(ADMIN_FIELD_NOT_ATTENDING_RSVPS, rsvpSummary.getNotAttendingRsvps());
            model.put(ADMIN_FIELD_LAST_UPDATED, rsvpSummary.getLastUpdated());
        } catch (Exception e) {
            log.error("Error building RSVP summary for admin notification", e);
            // Add error flag to model
            model.put(ADMIN_FIELD_SUMMARY_ERROR, true);
            model.put(ADMIN_FIELD_ERROR_MESSAGE, "Unable to process RSVP summary: " + e.getMessage());
        }

        return model;
    }

    /**
     * Build a model map for RSVP-related emails
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     * @return Map containing all template variables
     */
    private Map<String, Object> buildRsvpEmailModel(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        Map<String, Object> model = new HashMap<>();

        // Guest information
        model.put(EMAIL_FIELD_FIRST_NAME, guestEntity.getFirstName());
        model.put(EMAIL_FIELD_LAST_NAME, guestEntity.getLastName());
        model.put(EMAIL_FIELD_GUEST_EMAIL, guestEntity.getEmail());

        // RSVP information
        model.put(EMAIL_FIELD_ATTENDING, rsvpEntity.getAttending());
        model.put(EMAIL_FIELD_BRINGING_PLUS_ONE, rsvpEntity.getBringingPlusOne());
        model.put(EMAIL_FIELD_PLUS_ONE_NAME, rsvpEntity.getPlusOneName());
        model.put(EMAIL_FIELD_DIETARY_RESTRICTIONS, rsvpEntity.getDietaryRestrictions());
        model.put(EMAIL_FIELD_RSVP_ID, rsvpEntity.getId());

        // Add submission date formatted for display
        if (rsvpEntity.getSubmittedAt() != null) {
            model.put(EMAIL_FIELD_SUBMISSION_DATE,
                  rsvpEntity.getSubmittedAt().format(DATE_FORMATTER));
        }

        return model;
    }

    /**
     * Process a template with the provided model data.
     * @param templateName Name of the template to process
     * @param model Model data to use for dynamic data in email
     * @return Processed template as a String
     * @throws IOException if template cannot be found
     * @throws TemplateException if template fails processing
     */
    private String processTemplate(String templateName, Map<String, Object> model)
        throws IOException, TemplateException {

        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return writer.toString();
    }

    /**
     * Send an HTML email
     * @param emailAddress Recipient email address
     * @param emailSubject Email subject
     * @param htmlContent HTML content of the email
     * @throws MessagingException if there's an error sending the email
     */
    private void sendHtmlEmail(String emailAddress, String emailSubject, String htmlContent)
        throws MessagingException {

        log.info("Creating and sending email to: {}", emailAddress);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(emailAddress);
        helper.setSubject(emailSubject);
        helper.setText(htmlContent, true);

        String senderEmail = emailConfig.getSenderEmail();

        helper.setFrom(senderEmail);

        mailSender.send(mimeMessage);
        log.info("Email sent successfully to: {}", emailAddress);
    }

    /**
     * Sends a donation confirmation email to the donor
     * @param donation The donation entity
     */
    public void sendDonationConfirmationEmail(DonationEntity donation) {
        log.info("STARTED - Sending donation confirmation email to: {}", donation.getDonorEmail());

        if (StringUtils.isBlank(donation.getDonorEmail())) {
            log.warn("Cannot send donation confirmation email - donor email is invalid");
            return;
        }

        try {

            String templatePath = emailConfig.getDonationConfirmationTemplatePath();
            String subject = emailConfig.getDonationConfirmationSubject();

            Map<String, Object> model = buildDonationConfirmationEmailModel(donation);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(donation.getDonorEmail(), subject, htmlContent);

            log.info("COMPLETED - Donation confirmation email sent successfully to: {}", donation.getDonorEmail());

        } catch (IOException e) {
            log.error("Template not found for donation confirmation email", e);
            throw WeddingAppException.internalError("Email template not found: " + e.getMessage());
        } catch (TemplateException e) {
            log.error("Template processing error for donation confirmation email", e);
            throw WeddingAppException.internalError("Email template processing failed: " + e.getMessage());
        } catch (MessagingException e) {
            log.error("Failed to send donation confirmation email to: {}", donation.getDonorEmail(), e);
            throw WeddingAppException.internalError("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending donation confirmation email", e);
            throw WeddingAppException.internalError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Sends a thank you email to the donor using the configured template
     * @param donation The confirmed donation entity
     */
    public void sendDonationThankYouEmail(DonationEntity donation) {
        log.info("STARTED - Sending donation thank you email to: {}", donation.getDonorEmail());

        // Validate email
        if (StringUtils.isBlank(donation.getDonorEmail())) {
            log.warn("Cannot send thank you email - donor email is invalid");
            return;
        }

        if (donation.getStatus() != DonationStatus.CONFIRMED) {
            log.warn("Cannot send thank you email - donation is not confirmed");
            throw WeddingAppException.validationError("Cannot send thank you for unconfirmed donation");
        }

        try {
            // Get template path from config
            String templatePath = emailConfig.getDonationThankYouTemplatePath();

            // Fallback to constant if config value is not set
            if (StringUtils.isBlank(templatePath)) {
                templatePath = DONATION_THANK_YOU_TEMPLATE;
            }

            // Get subject from config
            String subject = emailConfig.getDonationThankYouSubject();

            // Fallback to constant if config value is not set
            if (StringUtils.isBlank(subject)) {
                subject = DONATION_THANK_YOU_SUBJECT;
            }

            // Build the template model
            Map<String, Object> model = buildDonationThankYouEmailModel(donation);

            // Process the template and send the email
            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(donation.getDonorEmail(), subject, htmlContent);

            log.info("COMPLETED - Thank you email sent successfully to: {}", donation.getDonorEmail());

        } catch (IOException e) {
            log.error("Template not found for donation thank you email", e);
            throw WeddingAppException.internalError("Email template not found: " + e.getMessage());
        } catch (TemplateException e) {
            log.error("Template processing error for donation thank you email", e);
            throw WeddingAppException.internalError("Email template processing failed: " + e.getMessage());
        } catch (MessagingException e) {
            log.error("Failed to send thank you email to: {}", donation.getDonorEmail(), e);
            throw WeddingAppException.internalError("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending donation thank you email", e);
            throw WeddingAppException.internalError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Build a model map for donation confirmation emails
     * @param donation The donation entity
     * @return Map containing all template variables
     */
    private Map<String, Object> buildDonationConfirmationEmailModel(DonationEntity donation) {
        Map<String, Object> model = new HashMap<>();

        // Donor information
        model.put(EMAIL_FIELD_DONOR_NAME, donation.getDonorName());
        model.put(EMAIL_FIELD_DONOR_EMAIL, donation.getDonorEmail());
        model.put(EMAIL_FIELD_DONOR_PHONE, donation.getDonorPhone());

        // Donation information
        model.put(EMAIL_FIELD_DONATION_AMOUNT, donation.getAmount());
        model.put(EMAIL_FIELD_PAYMENT_METHOD, donation.getPaymentMethod().getDisplayName());
        model.put(EMAIL_FIELD_PAYMENT_REFERENCE, donation.getPaymentReference());
        model.put(EMAIL_FIELD_DONATION_MESSAGE, donation.getMessage());
        model.put(EMAIL_FIELD_DONATION_ID, donation.getId());

        // Add donation date formatted for display
        if (donation.getDonationDate() != null) {
            model.put(EMAIL_FIELD_DONATION_DATE,
                    donation.getDonationDate().format(DATE_FORMATTER));
        }

        // Add status information
        model.put(EMAIL_FIELD_DONATION_STATUS, donation.getStatus().getDisplayName());

        // Add boolean flags for template conditionals
        model.put(EMAIL_FIELD_HAS_PAYMENT_REFERENCE, StringUtils.isNotBlank(donation.getPaymentReference()));
        model.put(EMAIL_FIELD_HAS_DONATION_MESSAGE, StringUtils.isNotBlank(donation.getMessage()));
        model.put(EMAIL_FIELD_HAS_PHONE, StringUtils.isNotBlank(donation.getDonorPhone()));

        return model;
    }

    /**
     * Build a model map for donation thank you emails
     * @param donation The donation entity
     * @return Map containing all template variables
     */
    private Map<String, Object> buildDonationThankYouEmailModel(DonationEntity donation) {
        Map<String, Object> model = new HashMap<>();

        // Donor information
        model.put(EMAIL_FIELD_DONOR_NAME, donation.getDonorName());
        model.put(EMAIL_FIELD_DONOR_EMAIL, donation.getDonorEmail());

        // Donation information
        model.put(EMAIL_FIELD_DONATION_AMOUNT, donation.getAmount());
        model.put(EMAIL_FIELD_PAYMENT_METHOD, donation.getPaymentMethod().getDisplayName());
        model.put(EMAIL_FIELD_DONATION_MESSAGE, donation.getMessage());
        model.put(EMAIL_FIELD_DONATION_ID, donation.getId());

        // Add donation and confirmation dates formatted for display
        if (donation.getDonationDate() != null) {
            model.put(EMAIL_FIELD_DONATION_DATE,
                    donation.getDonationDate().format(DATE_FORMATTER));
        }

        if (donation.getConfirmedDate() != null) {
            model.put(EMAIL_FIELD_CONFIRMED_DATE,
                    donation.getConfirmedDate().format(DATE_FORMATTER));
        }

        // Add boolean flags for template conditionals
        model.put(EMAIL_FIELD_HAS_DONATION_MESSAGE, StringUtils.isNotBlank(donation.getMessage()));
        
        return model;
    }
}

package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.config.EmailConfig;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.dto.RSVPSummaryDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.*;

/**
 * Service for handling all email functionality in the application
 *
 * pssx tmse xork ukhy
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
            // Get template path from config
            String templatePath = isAttending
                    ? emailConfig.getAttendingTemplatePath()
                    : emailConfig.getNotAttendingTemplatePath();

            // Fallback to constants if config values are not set
            if (StringUtils.isBlank(templatePath)) {
                templatePath = isAttending
                        ? ATTENDING_RSVP_CONFIRMATION
                        : NOT_ATTENDING_RSVP_CONFIRMATION;
            }

            // Get subject from config
            String subject = isAttending
                    ? emailConfig.getAttendingSubject()
                    : emailConfig.getNotAttendingSubject();

            // Fallback to constants if config values are not set
            if (StringUtils.isBlank(subject)) {
                subject = isAttending
                        ? ATTENDING_EMAIL_SUBJECT
                        : NOT_ATTENDING_EMAIL_SUBJECT;
            }

            // Build the template model
            Map<String, Object> model = buildRsvpEmailModel(rsvpEntity, guestEntity);

            // Process the template and send the email
            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(guestEntity.getEmail(), subject, htmlContent);

            // Admin notifications are now handled directly by RSVPService
            // to avoid circular dependencies

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
            model.put("totalRsvps", rsvpSummary.getTotalRsvps());
            model.put("totalAttending", rsvpSummary.getTotalAttending());
            model.put("totalNotAttending", rsvpSummary.getTotalNotAttending());
            model.put("totalGuests", rsvpSummary.getTotalGuests());
            model.put("attendingRsvps", rsvpSummary.getAttendingRsvps());
            model.put("notAttendingRsvps", rsvpSummary.getNotAttendingRsvps());
            model.put("lastUpdated", rsvpSummary.getLastUpdated());
        } catch (Exception e) {
            log.error("Error building RSVP summary for admin notification", e);
            // Add error flag to model
            model.put("summaryError", true);
            model.put("errorMessage", "Unable to process RSVP summary: " + e.getMessage());
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

        // Get sender email from config or use default
        String senderEmail = StringUtils.isNotBlank(emailConfig.getSenderEmail())
                ? emailConfig.getSenderEmail()
                : "caseythecoder90@gmail.com";

        helper.setFrom(senderEmail);

        mailSender.send(mimeMessage);
        log.info("Email sent successfully to: {}", emailAddress);
    }
}


















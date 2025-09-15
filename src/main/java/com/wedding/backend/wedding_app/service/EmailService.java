package com.wedding.backend.wedding_app.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.wedding.backend.wedding_app.annotations.EmailRetryable;
import com.wedding.backend.wedding_app.config.EmailConfig;
import com.wedding.backend.wedding_app.dto.RSVPSummaryDTO;
import com.wedding.backend.wedding_app.entity.DonationEntity;
import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.*;

/**
 * Service for handling all email functionality in the application
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final Resend resend;
    private final Configuration freemarkerConfig;
    private final EmailConfig emailConfig;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");


    /**
     * Sends an RSVP confirmation email based on the RSVP status (attending or not attending)
     * @param rsvpEntity The RSVP entity containing all needed data
     * @param guestEntity The guest entity
     */
    public void sendRSVPConfirmationEmail(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        sendRSVPConfirmationEmail(rsvpEntity, guestEntity, LANGUAGE_ENGLISH); // Default to English
    }

    /**
     * Sends an RSVP confirmation email with language preference
     * @param rsvpEntity The RSVP entity containing all needed data
     * @param guestEntity The guest entity
     * @param preferredLanguage The preferred language (LANGUAGE_ENGLISH or LANGUAGE_PORTUGUESE)
     */
    public void sendRSVPConfirmationEmail(RSVPEntity rsvpEntity, GuestEntity guestEntity, String preferredLanguage) {
        log.info("STARTED - Sending RSVP confirmation email to: {} in language: {}", 
                guestEntity.getEmail(), preferredLanguage);

        if (StringUtils.isBlank(guestEntity.getEmail())) {
            log.warn("Cannot send email - guest email is invalid");
            return;
        }

        boolean isAttending = BooleanUtils.isTrue(rsvpEntity.getAttending());
        boolean isPortuguese = StringUtils.equalsIgnoreCase(LANGUAGE_PORTUGUESE, preferredLanguage);

        String templatePath = null;
        try {
            String subject;
            
            if (BooleanUtils.isTrue(isAttending)) {
                templatePath = isPortuguese ? emailConfig.getAttendingTemplatePathPt() : emailConfig.getAttendingTemplatePath();
                subject = isPortuguese ? emailConfig.getAttendingSubjectPt() : emailConfig.getAttendingSubject();
            } else {
                templatePath = isPortuguese ? emailConfig.getNotAttendingTemplatePathPt() : emailConfig.getNotAttendingTemplatePath();
                subject = isPortuguese ? emailConfig.getNotAttendingSubjectPt() : emailConfig.getNotAttendingSubject();
            }

            if (StringUtils.isBlank(templatePath)) {
                log.warn("Portuguese template not configured, falling back to English for language: {}", preferredLanguage);
                templatePath = isAttending ? emailConfig.getAttendingTemplatePath() : emailConfig.getNotAttendingTemplatePath();
                subject = isAttending ? emailConfig.getAttendingSubject() : emailConfig.getNotAttendingSubject();
            }

            Map<String, Object> model = buildRsvpEmailModel(rsvpEntity, guestEntity, preferredLanguage);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(guestEntity.getEmail(), subject, htmlContent);
            
            log.info("COMPLETED - RSVP confirmation email sent successfully to: {} in language: {}", 
                    guestEntity.getEmail(), preferredLanguage);

        } catch (Exception e) {
            throw handleEmailException("RSVP confirmation", templatePath, e);
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

            if (StringUtils.isBlank(templatePath)) {
                templatePath = ADMIN_RSVP_NOTIFICATION;
            }

            String subject = emailConfig.getAdminNotificationSubject();

            if (StringUtils.isBlank(subject)) {
                subject = ADMIN_NOTIFICATION_SUBJECT;
            }

            Map<String, Object> model = buildAdminNotificationModel(rsvpEntity, guestEntity, rsvpSummary);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(emailConfig.getAdminEmail(), subject, htmlContent);
            
            log.info("COMPLETED - Admin notification sent successfully");

        } catch (Exception e) {
            log.error("Exception while sending admin notification: ", e);
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


        Map<String, Object> model = buildRsvpEmailModel(triggerRsvpEntity, triggerGuestEntity);

        try {

            model.put(ADMIN_FIELD_TOTAL_RSVPS, rsvpSummary.getTotalRsvps());
            model.put(ADMIN_FIELD_TOTAL_ATTENDING, rsvpSummary.getTotalAttending());
            model.put(ADMIN_FIELD_TOTAL_NOT_ATTENDING, rsvpSummary.getTotalNotAttending());
            model.put(ADMIN_FIELD_TOTAL_GUESTS, rsvpSummary.getTotalGuests());
            model.put(ADMIN_FIELD_ATTENDING_RSVPS, rsvpSummary.getAttendingRsvps());
            model.put(ADMIN_FIELD_NOT_ATTENDING_RSVPS, rsvpSummary.getNotAttendingRsvps());
            model.put(ADMIN_FIELD_LAST_UPDATED, rsvpSummary.getLastUpdated());
            

            model.put(ADMIN_FIELD_ATTENDING_FAMILY_MEMBERS, rsvpSummary.getAttendingFamilyMembers());
            log.info("Added {} attending family members to admin notification model", 
                    Objects.nonNull(rsvpSummary.getAttendingFamilyMembers()) ? rsvpSummary.getAttendingFamilyMembers().size() : 0);
            
        } catch (Exception e) {
            log.error("Error building RSVP summary for admin notification", e);
            model.put(ADMIN_FIELD_SUMMARY_ERROR, true);
            model.put(ADMIN_FIELD_ERROR_MESSAGE, "Unable to process RSVP summary: " + e.getMessage());
        }

        return model;
    }

    /**
     * Build a model map for RSVP-related emails including family member information
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     * @return Map containing all template variables
     */
    private Map<String, Object> buildRsvpEmailModel(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        return buildRsvpEmailModel(rsvpEntity, guestEntity, LANGUAGE_ENGLISH); // Default to English
    }

    /**
     * Build a model map for RSVP-related emails with language preference
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     * @param preferredLanguage The preferred language for date formatting
     * @return Map containing all template variables
     */
    private Map<String, Object> buildRsvpEmailModel(RSVPEntity rsvpEntity, GuestEntity guestEntity, String preferredLanguage) {
        Map<String, Object> model = new HashMap<>();

        model.put(EMAIL_FIELD_FIRST_NAME, guestEntity.getFirstName());
        model.put(EMAIL_FIELD_LAST_NAME, guestEntity.getLastName());
        model.put(EMAIL_FIELD_GUEST_EMAIL, guestEntity.getEmail());

        model.put(EMAIL_FIELD_ATTENDING, rsvpEntity.getAttending());
        model.put(EMAIL_FIELD_DIETARY_RESTRICTIONS, rsvpEntity.getDietaryRestrictions());
        model.put(EMAIL_FIELD_RSVP_ID, rsvpEntity.getId());

        if (Objects.nonNull(rsvpEntity.getSubmittedAt())) {
            model.put(EMAIL_FIELD_SUBMISSION_DATE,
                  rsvpEntity.getSubmittedAt().format(DATE_FORMATTER));
        }

        FamilyGroupEntity familyGroup = guestEntity.getFamilyGroup();
        boolean isFamilyRsvp = Objects.nonNull(familyGroup) && BooleanUtils.isTrue(guestEntity.getIsPrimaryContact());
        model.put(EMAIL_FIELD_IS_FAMILY_RSVP, isFamilyRsvp);
        
        if (BooleanUtils.isTrue(isFamilyRsvp)) {
            log.info("Adding family information to email model for group: {}", familyGroup.getGroupName());
            
            model.put(EMAIL_FIELD_FAMILY_GROUP_NAME, familyGroup.getGroupName());

            List<FamilyMemberEntity> familyMembers = familyGroup.getFamilyMembers();
            if (Objects.nonNull(familyMembers) && CollectionUtils.isNotEmpty(familyMembers)) {
                List<Map<String, Object>> familyMemberData = familyMembers.stream()
                        .map(this::buildFamilyMemberMap)
                        .collect(Collectors.toList());
                
                model.put(EMAIL_FIELD_FAMILY_MEMBERS, familyMemberData);
                
                long attendingCount = familyMembers.stream()
                    .filter(member -> BooleanUtils.isTrue(member.getIsAttending()))
                    .count();
                
                model.put(EMAIL_FIELD_FAMILY_ATTENDING_COUNT, attendingCount);
                model.put(EMAIL_FIELD_FAMILY_TOTAL_COUNT, familyMembers.size());
            } else {
                model.put(EMAIL_FIELD_FAMILY_MEMBERS, List.of());
                model.put(EMAIL_FIELD_FAMILY_ATTENDING_COUNT, 0);
                model.put(EMAIL_FIELD_FAMILY_TOTAL_COUNT, 0);
            }
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
     * Send an HTML email using Resend API
     * @param emailAddress Recipient email address
     * @param emailSubject Email subject
     * @param htmlContent HTML content of the email
     * @throws ResendException if there's an error sending the email
     */
    @EmailRetryable
    private void sendHtmlEmail(String emailAddress, String emailSubject, String htmlContent)
        throws ResendException {

        log.info("Creating and sending email to: {} via Resend API", emailAddress);

        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from(emailConfig.getSenderEmail())
                .to(emailAddress)
                .subject(emailSubject)
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(emailOptions);
            log.info("Email sent successfully to: {} with Resend ID: {}", emailAddress, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send email to: {} via Resend API", emailAddress, e);
            throw e;
        }
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

        String templatePath = null;
        try {
            templatePath = emailConfig.getDonationConfirmationTemplatePath();
            String subject = emailConfig.getDonationConfirmationSubject();

            Map<String, Object> model = buildDonationConfirmationEmailModel(donation);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(donation.getDonorEmail(), subject, htmlContent);

            log.info("COMPLETED - Donation confirmation email sent successfully to: {}", donation.getDonorEmail());

        } catch (Exception e) {
            throw handleEmailException("donation confirmation", templatePath, e);
        }
    }

    /**
     * Sends a thank-you email to the donor using the configured template
     * @param donation The confirmed donation entity
     */
    public void sendDonationThankYouEmail(DonationEntity donation) {
        log.info("STARTED - Sending donation thank you email to: {}", donation.getDonorEmail());

        if (StringUtils.isBlank(donation.getDonorEmail())) {
            log.warn("Cannot send thank you email - donor email is invalid");
            return;
        }

        if (BooleanUtils.isFalse(Objects.equals(donation.getStatus(), DonationStatus.CONFIRMED))) {
            log.warn("Cannot send thank you email - donation is not confirmed");
            throw WeddingAppException.validationError("Cannot send thank you for unconfirmed donation");
        }

        String templatePath = null;
        try {
            templatePath = emailConfig.getDonationThankYouTemplatePath();

            if (StringUtils.isBlank(templatePath)) {
                templatePath = DONATION_THANK_YOU_TEMPLATE;
            }

            String subject = emailConfig.getDonationThankYouSubject();

            if (StringUtils.isBlank(subject)) {
                subject = DONATION_THANK_YOU_SUBJECT;
            }

            Map<String, Object> model = buildDonationThankYouEmailModel(donation);

            String htmlContent = processTemplate(templatePath, model);
            sendHtmlEmail(donation.getDonorEmail(), subject, htmlContent);

            log.info("COMPLETED - Thank you email sent successfully to: {}", donation.getDonorEmail());

        } catch (Exception e) {
            throw handleEmailException("donation thank you", templatePath, e);
        }
    }

    /**
     * Build a model map for donation confirmation emails
     * @param donation The donation entity
     * @return Map containing all template variables
     */
    private Map<String, Object> buildDonationConfirmationEmailModel(DonationEntity donation) {
        Map<String, Object> model = new HashMap<>();

        model.put(EMAIL_FIELD_DONOR_NAME, donation.getDonorName());
        model.put(EMAIL_FIELD_DONOR_EMAIL, donation.getDonorEmail());
        model.put(EMAIL_FIELD_DONOR_PHONE, donation.getDonorPhone());

        model.put(EMAIL_FIELD_DONATION_AMOUNT, donation.getAmount());
        model.put(EMAIL_FIELD_PAYMENT_METHOD, donation.getPaymentMethod().getDisplayName());
        model.put(EMAIL_FIELD_PAYMENT_REFERENCE, donation.getPaymentReference());
        model.put(EMAIL_FIELD_DONATION_MESSAGE, donation.getMessage());
        model.put(EMAIL_FIELD_DONATION_ID, donation.getId());

        if (Objects.nonNull(donation.getDonationDate())) {
            model.put(EMAIL_FIELD_DONATION_DATE,
                    donation.getDonationDate().format(DATE_FORMATTER));
        }

        model.put(EMAIL_FIELD_DONATION_STATUS, donation.getStatus().getDisplayName());

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

        model.put(EMAIL_FIELD_DONOR_NAME, donation.getDonorName());
        model.put(EMAIL_FIELD_DONOR_EMAIL, donation.getDonorEmail());

        model.put(EMAIL_FIELD_DONATION_AMOUNT, donation.getAmount());
        model.put(EMAIL_FIELD_PAYMENT_METHOD, donation.getPaymentMethod().getDisplayName());
        model.put(EMAIL_FIELD_DONATION_MESSAGE, donation.getMessage());
        model.put(EMAIL_FIELD_DONATION_ID, donation.getId());

        if (Objects.nonNull(donation.getDonationDate())) {
            model.put(EMAIL_FIELD_DONATION_DATE,
                    donation.getDonationDate().format(DATE_FORMATTER));
        }

        if (Objects.nonNull(donation.getConfirmedDate())) {
            model.put(EMAIL_FIELD_CONFIRMED_DATE,
                    donation.getConfirmedDate().format(DATE_FORMATTER));
        }

        model.put(EMAIL_FIELD_HAS_DONATION_MESSAGE, StringUtils.isNotBlank(donation.getMessage()));
        
        return model;
    }

    /**
     * Send a confirmation email to the guest asynchronously
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     */
    @Async("emailTaskExecutor")
    public void sendGuestConfirmationEmailAsync(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        sendGuestConfirmationEmailAsync(rsvpEntity, guestEntity, LANGUAGE_ENGLISH); // Default to English
    }

    /**
     * Send a confirmation email to the guest asynchronously with language preference
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     * @param preferredLanguage The preferred language (LANGUAGE_ENGLISH or LANGUAGE_PORTUGUESE)
     */
    @Async("emailTaskExecutor")
    public void sendGuestConfirmationEmailAsync(RSVPEntity rsvpEntity, GuestEntity guestEntity, String preferredLanguage) {
        try {
            log.info("Asynchronously sending confirmation email to guest: {} in language: {}", 
                    guestEntity.getEmail(), preferredLanguage);
            sendRSVPConfirmationEmail(rsvpEntity, guestEntity, preferredLanguage);
            log.info("Successfully sent confirmation email to guest: {} in language: {}", 
                    guestEntity.getEmail(), preferredLanguage);
        } catch (Exception e) {
            log.error("Failed to send confirmation email to guest: {} in language: {}", 
                    guestEntity.getEmail(), preferredLanguage, e);
        }
    }

    /**
     * Send an admin notification email with RSVP summary asynchronously
     * @param rsvpEntity The RSVP entity that triggered the notification
     * @param guestEntity The guest entity
     * @param rsvpSummary The summary of all RSVPs
     */
    @Async("emailTaskExecutor")
    public void sendAdminNotificationEmailAsync(RSVPEntity rsvpEntity, GuestEntity guestEntity, RSVPSummaryDTO rsvpSummary) {
        try {
            if (BooleanUtils.isNotTrue(emailConfig.isSendAdminNotifications()) ||
                StringUtils.isBlank(emailConfig.getAdminEmail())) {
                log.info("Admin notifications are disabled or no admin email configured");
                return;
            }

            log.info("Asynchronously sending admin notification email");
            sendAdminRsvpNotification(rsvpEntity, guestEntity, rsvpSummary);
            log.info("Successfully sent admin notification email");
        } catch (Exception e) {
            log.error("Failed to send admin notification email", e);
        }
    }

    /**
     * Send donation confirmation email asynchronously
     * @param donation The donation entity
     */
    @Async("emailTaskExecutor")
    public void sendDonationConfirmationEmailAsync(DonationEntity donation) {
        try {
            log.info("Asynchronously sending donation confirmation email to: {}", donation.getDonorEmail());
            sendDonationConfirmationEmail(donation);
            log.info("Successfully sent donation confirmation email to: {}", donation.getDonorEmail());
        } catch (Exception e) {
            log.error("Failed to send donation confirmation email to: {}", donation.getDonorEmail(), e);
        }
    }

    /**
     * Send donation thank you email asynchronously
     * @param donation The donation entity
     */
    @Async("emailTaskExecutor")
    public void sendDonationThankYouEmailAsync(DonationEntity donation) {
        try {
            log.info("Asynchronously sending donation thank you email to: {}", donation.getDonorEmail());
            sendDonationThankYouEmail(donation);
            log.info("Successfully sent donation thank you email to: {}", donation.getDonorEmail());
        } catch (Exception e) {
            log.error("Failed to send donation thank you email to: {}", donation.getDonorEmail(), e);
        }
    }

    private Map<String, Object> buildFamilyMemberMap(FamilyMemberEntity member) {
        Map<String, Object> memberData = new HashMap<>();
        memberData.put(FAMILY_MEMBER_FIRST_NAME, member.getFirstName());
        memberData.put(FAMILY_MEMBER_LAST_NAME, member.getLastName());
        memberData.put(FAMILY_MEMBER_AGE_GROUP, member.getAgeGroup());
        memberData.put(FAMILY_MEMBER_IS_ATTENDING, member.getIsAttending());
        memberData.put(FAMILY_MEMBER_DIETARY_RESTRICTIONS, member.getDietaryRestrictions());
        return memberData;
    }

    /**
     * Centralized exception handling for email operations
     * @param operation The email operation being performed (e.g., "RSVP confirmation", "donation thank you")
     * @param templatePath The template path being used (can be null)
     * @param exception The exception that occurred
     * @return WeddingAppException to be thrown
     */
    private WeddingAppException handleEmailException(String operation, String templatePath, Exception exception) {
        if (exception instanceof IOException) {
            log.error("Template not found for {} email: {}", operation, templatePath, exception);
            return WeddingAppException.emailTemplateError(templatePath, exception);
        } else if (exception instanceof TemplateException) {
            log.error("Template processing error for {} email: {}", operation, templatePath, exception);
            return WeddingAppException.emailTemplateError(templatePath, exception);
        } else if (exception instanceof ResendException) {
            log.error("Failed to send {} email via Resend API", operation, exception);
            return WeddingAppException.emailSendError(operation, exception);
        } else {
            log.error("Unexpected error during {} email operation", operation, exception);
            return WeddingAppException.emailSendError(operation, exception.getMessage());
        }
    }

}

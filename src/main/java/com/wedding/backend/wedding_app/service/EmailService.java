package com.wedding.backend.wedding_app.service;

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
import java.util.HashMap;
import java.util.Map;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.ATTENDING_RSVP_CONFIRMATION;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.NOT_ATTENDING_RSVP_CONFIRMATION;

/*
 Use Springs JavaMailSender with MimeMessageHelper (handles HTML and attachments)
 - template engines : Thymeleaf or Freemarker
 - can store images in resources in static directory
 - pssx tmse xork ukhy
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender, Configuration freemarkerConfig) {
        this.mailSender = mailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    /**
     * Sends an RSVP confirmation email based on the RSVP status (attending or not attending)
     * @param rsvpEntity The RSVP entity containing all needed data
     * @param guestEntity The guest entity
     */
    public void sendRSVPConfirmationEmail(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        log.info("STARTED - Sending RSVP confirmation email to: {}", guestEntity.getEmail());

        // enhance validations on email
        if (StringUtils.isBlank(guestEntity.getEmail())) {
            log.warn("Cannot send email = guest email is invalid");
            return;
        }

        boolean isAttending = rsvpEntity.getAttending();

        try {

            String templateName = isAttending
                    ? ATTENDING_RSVP_CONFIRMATION
                    : NOT_ATTENDING_RSVP_CONFIRMATION;

            // probably create a method to do this
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", guestEntity.getFirstName());
            model.put("lastName", guestEntity.getLastName());
            model.put("attending", rsvpEntity.getAttending());
            model.put("bringingPlusOne", rsvpEntity.getBringingPlusOne());
            model.put("plusOneName", rsvpEntity.getPlusOneName());
            model.put("dietaryRestrictions", rsvpEntity.getDietaryRestrictions());

            // add to constants
            String subject = isAttending
                    ? "Wedding RSVP Confirmation - We're excited to see you!"
                    : "Wedding RSVP Confirmation - Thank you for your response!";

            String htmlContent = processTemplate(templateName, model);

            sendHtmlEmail(guestEntity.getEmail(), subject, htmlContent);

        } catch (Exception e) {
            log.error("Exception while sending RSVP email: ", e);
            throw new RuntimeException(e);
        }
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

    private void sendHtmlEmail(String emailAddress, String emailSubject, String htmlContent)
        throws MessagingException {

        log.info("Creating and sending MimeMessage with generated template");

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(emailAddress);
        helper.setSubject(emailSubject);
        helper.setText(htmlContent, true);
        helper.setFrom("caseythecoder90@gmail.com");

        mailSender.send(mimeMessage);

        log.info("Email MimeMessage sent successfully");
    }
}


















package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.DebugApiDocs;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.model.reponse.DebugResponse;
import com.wedding.backend.wedding_app.service.EmailService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/debug")
@Tag(name = "Debug Operations", description = "Endpoints for testing and debugging application features")
public class DebugController {
    
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(DebugController.class);
    
    public DebugController(EmailService emailService) {
        this.emailService = emailService;
    }

    @DebugApiDocs.DebugCors
    @GetMapping("/cors")
    public Map<String, String> debugCors(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> debug = new HashMap<>();
        debug.put("origin", request.getHeader("Origin"));
        debug.put("cors_enabled", "true");
        return debug;
    }
    
    @DebugApiDocs.TestEmail
    @GetMapping(value = "/test-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DebugResponse> testEmail(
            @Parameter(description = "Email address to send the test to", required = true) 
            @RequestParam String email,
            
            @Parameter(description = "Whether to test the 'attending' (true) or 'not attending' (false) template", required = false) 
            @RequestParam(defaultValue = "true") boolean attending) {
        
        log.info("STARTED - Sending test email to: {}", email);
        
        try {
            // Create a test guest using builder
            GuestEntity testGuest = GuestEntity.builder()
                    .firstName("Yasmim")
                    .lastName("Sasahara")
                    .email(email)
                    .plusOneAllowed(true)
                    .build();
            
            // Create a test RSVP using builder
            RSVPEntity testRsvp = RSVPEntity.builder()
                    .attending(attending)
                    .bringingPlusOne(attending) // Set to true only if attending
                    .plusOneName(attending ? "Casey" : null)
                    .dietaryRestrictions(attending ? "No gluten, please" : null)
                    .submittedAt(OffsetDateTime.now())
                    .build();
            
            // Set bidirectional relationship
            testRsvp.setGuest(testGuest);
            
            // Send the test email
            emailService.sendRSVPConfirmationEmail(testRsvp, testGuest);
            
            // Build the response using builder pattern
            DebugResponse response = DebugResponse.builder()
                    .status("success")
                    .message("Test email sent successfully to " + email)
                    .timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .details("Template used: " + (attending ? "attending" : "not-attending"))
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to send test email", e);
            
            // Build error response using builder pattern
            DebugResponse response = DebugResponse.builder()
                    .status("error")
                    .message("Failed to send test email")
                    .timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .details(e.getMessage())
                    .build();
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
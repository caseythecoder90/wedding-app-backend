package com.wedding.backend.wedding_app.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotations for Debug API documentation
 */
public class DebugApiDocs {

    /**
     * Documentation for the test email endpoint
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Test email templates",
        description = "Sends a test email using the wedding RSVP templates. This endpoint allows testing of the email functionality without creating an actual RSVP."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Email sent successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.wedding.backend.wedding_app.model.reponse.DebugResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to send email",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.wedding.backend.wedding_app.model.reponse.DebugResponse.class))
        )
    })
    public @interface TestEmail {
    }

    /**
     * Documentation for the CORS debug endpoint
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Check CORS configuration", 
        description = "Test endpoint to verify CORS headers are working properly"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "CORS configuration information", 
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface DebugCors {
    }
    
    /**
     * Documentation for the test QR code generation endpoint
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Generate test QR code", 
        description = "Generates a QR code based on a provided code string. The QR code will link to the RSVP page with the code embedded."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "QR code generated successfully", 
            content = @Content(mediaType = "image/png")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to generate QR code"
        )
    })
    public @interface TestQRCode {
    }
}
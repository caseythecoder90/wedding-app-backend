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
 * Custom annotations for Invitation API documentation
 */
public class InvitationApiDocs {

    /**
     * Documentation for generating invitation codes
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Generate a new invitation code", 
        description = "Creates a new invitation code for a guest. The code can be used for RSVP authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Invitation code created successfully",
            content = @Content(mediaType = "application/json", 
                      schema = @Schema(implementation = com.wedding.backend.wedding_app.dto.InvitationCodeResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Guest not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error creating invitation code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface GenerateCode {
    }

    /**
     * Documentation for generating QR codes
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Get QR code for invitation", 
        description = "Generates a QR code image for a specific invitation code. " +
                      "The QR code will encode a URL to the RSVP page with the code embedded."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "QR code generated successfully",
            content = @Content(mediaType = "image/png")
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Invalid invitation code",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error generating QR code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface GetQRCode {
    }

    /**
     * Documentation for validating invitation codes
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Validate invitation code",
        description = "Validates an invitation code and returns the associated guest details and any existing RSVP data. " +
                      "Used for pre-filling guest information in the RSVP form and allowing users to edit existing RSVPs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Valid invitation code",
            content = @Content(mediaType = "application/json",
                      schema = @Schema(implementation = com.wedding.backend.wedding_app.dto.InvitationValidationResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Invalid invitation code",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Expired invitation code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface ValidateCode {
    }

    /**
     * Documentation for getting codes for a guest
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Get invitation codes for guest", 
        description = "Returns all invitation codes for a specific guest, including primary and replacement codes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of invitation codes",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Guest not found",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface GetCodesForGuest {
    }

    /**
     * Documentation for generating replacement codes
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Generate replacement code", 
        description = "Generates a replacement invitation code for a guest. " +
                      "Useful when the original invitation is lost or damaged."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Replacement code created successfully",
            content = @Content(mediaType = "application/json", 
                      schema = @Schema(implementation = com.wedding.backend.wedding_app.dto.InvitationCodeResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Guest not found",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error creating replacement code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface GenerateReplacementCode {
    }

    /**
     * Documentation for marking codes as used
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Mark invitation code as used", 
        description = "Marks an invitation code as used after RSVP submission. " +
                      "Optional feature to prevent multiple submissions with the same code."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Invitation code marked as used successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Invalid invitation code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface MarkCodeAsUsed {
    }

    /**
     * Documentation for generating QR codes for custom URLs
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Generate QR code for any URL", 
        description = "Creates a QR code that encodes the provided URL. " +
                      "Can be used for wedding website, social media links, registry links, or any custom URL. " +
                      "Returns a PNG image file that can be downloaded and shared."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "QR code generated successfully",
            content = @Content(mediaType = "image/png")
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid URL parameter",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error generating QR code",
            content = @Content(mediaType = "application/json")
        )
    })
    public @interface GenerateQRCodeForUrl {
    }
}
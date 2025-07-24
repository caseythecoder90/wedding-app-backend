package com.wedding.backend.wedding_app.annotations;

import com.wedding.backend.wedding_app.dto.DonationResponseDTO;
import com.wedding.backend.wedding_app.model.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DonationApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Submit a donation",
            description = "Submit a new donation to the honeymoon registry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Donation submitted successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid donation data",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate donation detected",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface SubmitDonation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get all donations",
            description = "Retrieve all donations with optional status filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Donations retrieved successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetAllDonations {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get donation by ID",
            description = "Retrieve a specific donation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Donation found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Donation not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetDonationById {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Confirm donation",
            description = "Confirm a pending donation after payment verification (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Donation confirmed successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Donation not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface ConfirmDonation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Send thank you email",
            description = "Send a thank you email for a confirmed donation (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thank you email sent successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid donation status or thank you already sent",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Donation not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface SendThankYouEmail {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get donations needing thank you",
            description = "Retrieve donations that are confirmed but haven't had thank you emails sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Donations retrieved successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DonationResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetDonationsNeedingThankYou {}
}
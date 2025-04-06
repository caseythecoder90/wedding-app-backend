package com.wedding.backend.wedding_app.annotations;

import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
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

public class RSVPApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get RSVP by guest ID", 
              description = "Retrieves RSVP information for a specific guest if it exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "RSVP found",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = RSVPResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "RSVP not found",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetRSVPByGuestId {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Submit or update RSVP", 
              description = "Creates a new RSVP or updates an existing one for a guest")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "RSVP updated",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = RSVPResponseDTO.class))),
        @ApiResponse(responseCode = "201", description = "RSVP created",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = RSVPResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Guest not found",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface SubmitOrUpdateRSVP {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Delete RSVP", 
              description = "Deletes an RSVP by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "RSVP deleted",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "RSVP not found",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface DeleteRSVP {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get all RSVPs", 
              description = "Retrieves all RSVPs in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of RSVPs retrieved",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = RSVPResponseDTO.class)))
    })
    public @interface GetAllRSVPs {}
}

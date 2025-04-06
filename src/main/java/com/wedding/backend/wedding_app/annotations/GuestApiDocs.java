package com.wedding.backend.wedding_app.annotations;

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

public class GuestApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Create a new guest", description = "Creates a new guest entry in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Guest successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface CreateGuest {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Find guest by name", description = "Searches for a guest using their first and last name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the guest"),
            @ApiResponse(responseCode = "404", description = "Guest not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface FindGuestByName {}
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get guest by ID", description = "Retrieves a guest using their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the guest"),
            @ApiResponse(responseCode = "404", description = "Guest not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetGuestById {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Update a guest", description = "Updates an existing guest's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Guest successfully updated"),
            @ApiResponse(responseCode = "404", description = "Guest not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface UpdateGuest {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Delete a guest", description = "Removes a guest from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Guest successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Guest not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface DeleteGuest {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get all guests", description = "Retrieves a list of all registered guests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of guests retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetAllGuests {}
}
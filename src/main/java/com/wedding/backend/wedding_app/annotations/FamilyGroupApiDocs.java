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

public class FamilyGroupApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Create a new family group", description = "Creates a complete family group with primary contact, additional guests, and family members")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Family group successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface CreateFamilyGroup {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get all family groups", description = "Retrieves a list of all family groups")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Family groups retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetAllFamilyGroups {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get family group by ID", description = "Retrieves a specific family group by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Family group found"),
            @ApiResponse(responseCode = "404", description = "Family group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetFamilyGroupById {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Add family member", description = "Adds a new family member to an existing family group")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Family member successfully added"),
            @ApiResponse(responseCode = "404", description = "Family group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface AddFamilyMember {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get family members", description = "Retrieves all family members for a specific family group")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Family members retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Family group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetFamilyMembers {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Delete family group", description = "Deletes a family group and all associated data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Family group successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Family group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface DeleteFamilyGroup {}
}
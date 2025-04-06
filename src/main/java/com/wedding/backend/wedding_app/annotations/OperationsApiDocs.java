package com.wedding.backend.wedding_app.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public @interface OperationsApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Add a new error definition",
            description = "Adds a new error definition to the database and in-memory cache"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Error definition created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Error key already exists")
    })
    @interface AddErrorDefinition {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update an error definition",
            description = "Updates an existing error definition"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Error definition updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Error key not found")
    })
    @interface UpdateErrorDefinition {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete an error definition",
            description = "Deletes an existing error definition"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Error definition deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Error key not found")
    })
    @interface DeleteErrorDefinition {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get all error definitions",
            description = "Returns all error definitions currently loaded in memory"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Error definitions retrieved successfully")
    })
    @interface GetAllErrorDefinitions {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Refresh error definitions",
            description = "Refreshes the in-memory cache of error definitions from the database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Error definitions refreshed successfully")
    })
    @interface RefreshErrorDefinitions {}
}
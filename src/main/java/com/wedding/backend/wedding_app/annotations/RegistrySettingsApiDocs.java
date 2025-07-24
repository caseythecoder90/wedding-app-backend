package com.wedding.backend.wedding_app.annotations;

import com.wedding.backend.wedding_app.dto.RegistrySettingsDTO;
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

public class RegistrySettingsApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get active registry settings",
            description = "Retrieves the currently active registry configuration including goal amount and payment handles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registry settings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistrySettingsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetActiveRegistrySettings {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Update registry settings",
            description = "Updates the registry configuration (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registry settings updated successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistrySettingsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface UpdateRegistrySettings {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Toggle registry status",
            description = "Activates or deactivates the registry (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registry status toggled successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistrySettingsDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface ToggleRegistryStatus {}
}
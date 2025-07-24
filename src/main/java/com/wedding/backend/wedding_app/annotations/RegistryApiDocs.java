package com.wedding.backend.wedding_app.annotations;

import com.wedding.backend.wedding_app.dto.RegistryOverviewDTO;
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

public class RegistryApiDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(summary = "Get registry overview",
            description = "Retrieve complete registry overview including settings, progress, and donation statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registry overview retrieved successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistryOverviewDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public @interface GetRegistryOverview {}
}
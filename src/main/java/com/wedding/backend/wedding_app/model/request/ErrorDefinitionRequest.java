package com.wedding.backend.wedding_app.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ErrorDefinitionRequest {

    @NotBlank(message = "Error key is required")
    @Pattern(regexp = "^[A-Z_]+$", message = "Error key must be in uppercase with underscores")
    private String errorKey;

    @NotBlank(message = "Error code is required")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}$", message = "Error code must be in format XXX.XXX")
    private String errorCode;

    @NotBlank(message = "Error reason is required")
    private String errorReason;

    @NotBlank(message = "Error message is required")
    private String errorMessage;

    private boolean retryable;
}
package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.entity.ErrorDefinition;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.model.exception.Detail;
import com.wedding.backend.wedding_app.model.exception.ErrorResponse;
import com.wedding.backend.wedding_app.model.request.ErrorDefinitionRequest;
import com.wedding.backend.wedding_app.repository.ErrorRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ErrorManagementService {

    private final ErrorRepository errorRepository;
    private final Map<String, ErrorDefinition> errorDefinitions = new ConcurrentHashMap<>();
    private final Logger log = LoggerFactory.getLogger(ErrorManagementService.class);
    
    public ErrorManagementService(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @PostConstruct
    public void loadErrors() {
        log.info("Loading error definitions from database");

        errorRepository.findAll().forEach(error ->
                errorDefinitions.put(error.getErrorKey(), error));

        log.info("Loaded {} error definitions", errorDefinitions.size());
    }

    /**
     * Adds a new error definition
     */
    public ErrorDefinition addErrorDefinition(ErrorDefinitionRequest request) {

        if (errorDefinitions.containsKey(request.getErrorKey())) {
            throw new WeddingAppException("ERROR_KEY_ALREADY_EXISTS", request.getErrorKey());
        }

        ErrorDefinition errorDefinition = ErrorDefinition.builder()
                .errorKey(request.getErrorKey())
                .errorCode(request.getErrorCode())
                .errorReason(request.getErrorReason())
                .errorMessage(request.getErrorMessage())
                .retryable(request.isRetryable())
                .build();

        ErrorDefinition savedError = errorRepository.save(errorDefinition);

        errorDefinitions.put(savedError.getErrorKey(), savedError);

        log.info("Added new error definition: {}", savedError.getErrorKey());
        return savedError;
    }

    /**
     * Updates an existing error definition
     */
    public ErrorDefinition updateErrorDefinition(String errorKey, ErrorDefinitionRequest request) {

        if (!errorDefinitions.containsKey(errorKey)) {
            throw new WeddingAppException("ERROR_KEY_NOT_FOUND", errorKey);
        }

        ErrorDefinition errorDefinition = ErrorDefinition.builder()
                .errorKey(errorKey)
                .errorCode(request.getErrorCode())
                .errorReason(request.getErrorReason())
                .errorMessage(request.getErrorMessage())
                .retryable(request.isRetryable())
                .build();

        ErrorDefinition updatedError = errorRepository.save(errorDefinition);

        errorDefinitions.put(updatedError.getErrorKey(), updatedError);

        log.info("Updated error definition: {}", updatedError.getErrorKey());
        return updatedError;
    }

    /**
     * Deletes an error definition
     */
    public void deleteErrorDefinition(String errorKey) {

        if (!errorDefinitions.containsKey(errorKey)) {
            throw new WeddingAppException("ERROR_KEY_NOT_FOUND", errorKey);
        }

        errorRepository.deleteById(errorKey);

        errorDefinitions.remove(errorKey);

        log.info("Deleted error definition: {}", errorKey);
    }

    /**
     * Gets all error definitions currently loaded in memory
     */
    public Map<String, ErrorDefinition> getAllErrorDefinitions() {
        // Return an unmodifiable map to prevent external modifications
        return Collections.unmodifiableMap(errorDefinitions);
    }

    /**
     * Refreshes the in-memory cache of error definitions from the database
     */
    public void refreshErrorDefinitions() {
        log.info("Refreshing error definitions from database");

        errorDefinitions.clear();

        errorRepository.findAll().forEach(error ->
                errorDefinitions.put(error.getErrorKey(), error));
        log.info("Refreshed {} error definitions", errorDefinitions.size());
    }

    /**
     * Creates an error response based on the error key
     */
    public ErrorResponse createErrorResponse(String errorKey, String path, HttpStatus status) {
        return createErrorResponse(errorKey, path, null, status);
    }

    /**
     * Creates an error response with additional details
     */
    public ErrorResponse createErrorResponse(String errorKey, String path, List<Detail> details, HttpStatus status) {
        ErrorDefinition errorDef = errorDefinitions.getOrDefault(errorKey,
                createDefaultError(errorKey));

        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorDef.getErrorCode())
                .errorMessage(errorDef.getErrorMessage())
                .errorReason(errorDef.getErrorReason())
                .path(path)
                .timestamp(LocalDateTime.now())
                .errorId(UUID.randomUUID().toString())
                .retryable(errorDef.isRetryable())
                .details(details)
                .build();
    }

    /**
     * Creates a default error when the error key is not found
     */
    private ErrorDefinition createDefaultError(String errorKey) {
        log.warn("Error definition not found for key: {}. Using default error.", errorKey);
        return ErrorDefinition.builder()
                .errorKey(errorKey)
                .errorCode("500.999")
                .errorReason("Unknown error occurred")
                .errorMessage("An unexpected error occurred. Please contact support.")
                .retryable(false)
                .build();
    }


}

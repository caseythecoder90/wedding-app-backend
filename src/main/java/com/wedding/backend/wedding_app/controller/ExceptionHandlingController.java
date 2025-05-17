package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.exception.DatabaseException;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.model.exception.Detail;
import com.wedding.backend.wedding_app.model.exception.ErrorResponse;
import com.wedding.backend.wedding_app.service.ErrorManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wedding.backend.wedding_app.util.WeddingErrorConstants.*;

/**
 * Global exception handler for the wedding application
 * Handles all exceptions in a standardized way
 */
@RestControllerAdvice
public class ExceptionHandlingController {

    private final ErrorManagementService errorManagementService;
    private final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    public ExceptionHandlingController(ErrorManagementService errorManagementService) {
        this.errorManagementService = errorManagementService;
    }

    /**
     * Handles WeddingAppException
     * Maps the error key to an appropriate HTTP status and builds a standardized response
     */
    @ExceptionHandler(WeddingAppException.class)
    public ResponseEntity<ErrorResponse> handleWeddingAppException(
            WeddingAppException ex, HttpServletRequest request) {
        
        log.error("WeddingAppException: {}", ex.getMessage());
        
        HttpStatus status = mapErrorKeyToStatus(ex.getErrorKey());
        String path = request.getRequestURI();
        
        // Create detail from exception details if available
        List<Detail> details = null;
        if (ex.getDetails() != null) {
            details = new ArrayList<>();
            if (ex.getDetails() instanceof String) {
                details.add(Detail.create("detail", ex.getDetails().toString()));
            } else {
                details.add(Detail.create("detail", Objects.toString(ex.getDetails())));
            }
        }
        
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                ex.getErrorKey(), path, details, status);
        
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handles database exceptions
     */
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            DatabaseException ex, HttpServletRequest request) {
        
        log.error("DatabaseException: {}", ex.getMessage(), ex);
        
        String path = request.getRequestURI();
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                DATABASE_ERROR, path, HttpStatus.INTERNAL_SERVER_ERROR);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handles validation exceptions from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.error("Validation exception: {}", ex.getMessage());
        
        String path = request.getRequestURI();
        BindingResult result = ex.getBindingResult();
        List<Detail> details = new ArrayList<>();
        
        for (FieldError fieldError : result.getFieldErrors()) {
            details.add(Detail.create(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            ));
        }
        
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                VALIDATION_ERROR, path, details, HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles malformed JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        log.error("Invalid request body: {}", ex.getMessage());
        
        String path = request.getRequestURI();
        List<Detail> details = new ArrayList<>();
        details.add(Detail.create("requestBody", "Invalid or malformed JSON"));
        
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                VALIDATION_ERROR, path, details, HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles type mismatch exceptions (e.g. passing string where number expected)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        log.error("Type mismatch: {}", ex.getMessage());
        
        String path = request.getRequestURI();
        List<Detail> details = new ArrayList<>();
        details.add(Detail.create(
                ex.getName(),
                "Invalid type provided. Expected: " + ex.getRequiredType().getSimpleName()
        ));
        
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                INVALID_PARAMETER, path, details, HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Fallback handler for all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Unhandled exception: ", ex);
        
        String path = request.getRequestURI();
        ErrorResponse errorResponse = errorManagementService.createErrorResponse(
                INTERNAL_SERVER_ERROR, path, HttpStatus.INTERNAL_SERVER_ERROR);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maps error keys to appropriate HTTP status codes
     */
    private HttpStatus mapErrorKeyToStatus(String errorKey) {
        return switch (errorKey) {
            case GUEST_NOT_FOUND, GUEST_NAME_NOT_FOUND, RSVP_NOT_FOUND,
                 ENTITY_NOT_FOUND, ERROR_KEY_NOT_FOUND, 
                 INVALID_INVITATION_CODE, EXPIRED_INVITATION_CODE, 
                 USED_INVITATION_CODE -> HttpStatus.NOT_FOUND;
                 
            case GUEST_ALREADY_EXISTS, RSVP_ALREADY_SUBMITTED, 
                 ERROR_KEY_ALREADY_EXISTS -> HttpStatus.CONFLICT;
                 
            case INVALID_PARAMETER, MISSING_PARAMETER, 
                 VALIDATION_ERROR, INVITATION_CODE_CREATION_ERROR -> HttpStatus.BAD_REQUEST;
                 
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            
            case DATABASE_ERROR, INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
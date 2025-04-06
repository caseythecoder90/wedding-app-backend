package com.wedding.backend.wedding_app.controller;


import com.wedding.backend.wedding_app.annotations.OperationsApiDocs;
import com.wedding.backend.wedding_app.entity.ErrorDefinition;
import com.wedding.backend.wedding_app.model.request.ErrorDefinitionRequest;
import com.wedding.backend.wedding_app.service.ErrorManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/operations")
public class ErrorManagementController {

    private final ErrorManagementService errorService;

    @PostMapping("/errors")
    @OperationsApiDocs.AddErrorDefinition
    public ResponseEntity<ErrorDefinition> addErrorDefinition(
            @Valid @RequestBody ErrorDefinitionRequest request) {

        log.info("Adding new error definition: {}", request.getErrorKey());
        ErrorDefinition errorDefinition = errorService.addErrorDefinition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(errorDefinition);
    }

    @PutMapping("/errors/{errorKey}")
    @OperationsApiDocs.UpdateErrorDefinition
    public ResponseEntity<ErrorDefinition> updateErrorDefinition(
            @PathVariable String errorKey,
            @Valid @RequestBody ErrorDefinitionRequest request) {

        log.info("Updating error definition: {}", errorKey);
        ErrorDefinition errorDefinition = errorService.updateErrorDefinition(errorKey, request);
        return ResponseEntity.ok(errorDefinition);
    }

    @DeleteMapping("/errors/{errorKey}")
    @OperationsApiDocs.DeleteErrorDefinition
    public ResponseEntity<Void> deleteErrorDefinition(@PathVariable String errorKey) {
        log.info("Deleting error definition: {}", errorKey);
        errorService.deleteErrorDefinition(errorKey);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/errors")
    @OperationsApiDocs.GetAllErrorDefinitions
    public ResponseEntity<Map<String, ErrorDefinition>> getAllErrorDefinitions() {
        log.info("Fetching all error definitions");
        return ResponseEntity.ok(errorService.getAllErrorDefinitions());
    }

    @PostMapping("/errors/refresh")
    @OperationsApiDocs.RefreshErrorDefinitions
    public ResponseEntity<Void> refreshErrorDefinitions() {
        log.info("Refreshing error definitions");
        errorService.refreshErrorDefinitions();
        return ResponseEntity.ok().build();
    }
}

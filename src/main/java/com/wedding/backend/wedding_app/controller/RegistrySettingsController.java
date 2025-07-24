package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.RegistrySettingsApiDocs;
import com.wedding.backend.wedding_app.dto.RegistrySettingsDTO;
import com.wedding.backend.wedding_app.dto.RegistrySettingsRequestDTO;
import com.wedding.backend.wedding_app.service.RegistrySettingsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/registry/settings")
@Tag(name = "Registry Settings", description = "APIs for managing registry configuration")
public class RegistrySettingsController {

    private final RegistrySettingsService registrySettingsService;
    private final Logger log = LoggerFactory.getLogger(RegistrySettingsController.class);

    public RegistrySettingsController(RegistrySettingsService registrySettingsService) {
        this.registrySettingsService = registrySettingsService;
    }

    /**
     * Get active registry settings - This endpoint will be called by frontend
     * Results are cached for 1 hour
     */
    @GetMapping
    @RegistrySettingsApiDocs.GetActiveRegistrySettings
    public ResponseEntity<RegistrySettingsDTO> getActiveRegistrySettings() {
        log.info("BEGIN - GET /v1/api/registry/settings - Fetching active registry settings");

        RegistrySettingsDTO settings = registrySettingsService.getActiveRegistrySettings();

        log.info("END - Registry settings fetched successfully");
        return ResponseEntity.ok(settings);
    }

    /**
     * Update registry settings (admin only) - Clears cache automatically
     */
    @PutMapping
    @RegistrySettingsApiDocs.UpdateRegistrySettings
    public ResponseEntity<RegistrySettingsDTO> updateRegistrySettings(
            @Valid @RequestBody RegistrySettingsRequestDTO request) {
        log.info("BEGIN - PUT /v1/api/registry/settings - Updating registry settings");

        RegistrySettingsDTO updated = registrySettingsService.updateRegistrySettings(request);

        log.info("END - Registry settings updated successfully");
        return ResponseEntity.ok(updated);
    }

    /**
     * Toggle registry active status (admin only)
     */
    @PutMapping("/toggle/{isActive}")
    @RegistrySettingsApiDocs.ToggleRegistryStatus
    public ResponseEntity<RegistrySettingsDTO> toggleRegistryStatus(
            @Parameter(description = "New active status", required = true)
            @PathVariable boolean isActive) {
        log.info("BEGIN - PUT /v1/api/registry/settings/toggle/{} - Toggling registry status", isActive);

        RegistrySettingsDTO updated = registrySettingsService.toggleRegistryStatus(isActive);

        log.info("END - Registry status toggled successfully");
        return ResponseEntity.ok(updated);
    }
}

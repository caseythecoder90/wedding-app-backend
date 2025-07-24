package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.RegistryApiDocs;
import com.wedding.backend.wedding_app.dto.RegistryOverviewDTO;
import com.wedding.backend.wedding_app.service.RegistryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/registry")
@Tag(name = "Honeymoon Registry", description = "Main registry APIs for public and admin use")
public class RegistryController {

    private final RegistryService registryService;
    private final Logger log = LoggerFactory.getLogger(RegistryController.class);

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    /**
     * Get complete registry overview with progress and statistics
     * This is the main endpoint for the frontend registry page
     */
    @GetMapping("/overview")
    @RegistryApiDocs.GetRegistryOverview
    public ResponseEntity<RegistryOverviewDTO> getRegistryOverview() {
        log.info("BEGIN - GET /v1/api/registry/overview - Fetching registry overview");

        RegistryOverviewDTO overview = registryService.getRegistryOverview();

        log.info("END - Registry overview fetched successfully. Progress: {}%", overview.getProgressPercentage());
        return ResponseEntity.ok(overview);
    }
}
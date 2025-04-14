package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.GuestApiDocs;
import com.wedding.backend.wedding_app.dto.GuestResponseDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.service.GuestService;
import com.wedding.backend.wedding_app.model.request.GuestRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/api/guests")
@Tag(name = "Guest Management", description = "APIs for managing wedding guests")
public class GuestController {

    private final GuestService guestService;
    private final Logger log = LoggerFactory.getLogger(GuestController.class);
    
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Create a new guest
     * @param request Guest details request
     * @return Created guest entity
     */
    @PostMapping("/create")
    @GuestApiDocs.CreateGuest
    public ResponseEntity<GuestEntity> createGuest(@RequestBody GuestRequest request) {
        log.info("BEGIN - Received request to create guest: {}", request);

        GuestEntity guest = guestService.addGuest(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.isPlusOneAllowed()
        );

        log.info("END - Guest created with ID: {}", guest.getId());

        return ResponseEntity.status(CREATED).body(guest);
    }

    /**
     * Find guest by name
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @return Guest entity
     */
    @GetMapping("/search")
    @GuestApiDocs.FindGuestByName
    public ResponseEntity<GuestEntity> findGuestByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        log.info("BEGIN - Searching for guest: {} {}", firstName, lastName);

        GuestEntity guest = guestService.findGuestByName(firstName, lastName);

        log.info("END - Found guest with ID: {}", guest.getId());

        return ResponseEntity.status(OK).body(guest);
    }

    /**
     * Verify guest and return DTO
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @return Guest response DTO
     */
    @GetMapping("/verify")
    @GuestApiDocs.FindGuestByName
    public ResponseEntity<GuestResponseDTO> verifyGuest(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        log.info("BEGIN - Verifying guest: {} {}", firstName, lastName);

        GuestResponseDTO guestResponse = guestService.getVerifiedGuest(firstName, lastName);

        log.info("END - Guest verified with ID: {}", guestResponse.getId());

        return ResponseEntity.status(OK).body(guestResponse);
    }

    /**
     * Get all guests
     * @return List of guest entities
     */
    @GetMapping
    @GuestApiDocs.GetAllGuests
    public ResponseEntity<List<GuestEntity>> getAllGuests() {
        log.info("BEGIN - Fetching all guests");

        List<GuestEntity> allGuests = guestService.getAllGuests();

        log.info("END - {} guests found", allGuests.size());

        return ResponseEntity.status(OK).body(allGuests);
    }

    /**
     * Get guest by ID
     * @param id Guest ID
     * @return Guest entity
     */
    @GetMapping("/{id}")
    @GuestApiDocs.GetGuestById
    public ResponseEntity<GuestEntity> getGuestById(@PathVariable Long id) {
        log.info("BEGIN - Fetching guest with ID: {}", id);

        GuestEntity guest = guestService.findById(id);

        log.info("END - Found guest: {} {}", guest.getFirstName(), guest.getLastName());

        return ResponseEntity.status(OK).body(guest);
    }

    /**
     * Update guest
     * @param id Guest ID
     * @param request Updated guest details
     * @return Updated guest entity
     */
    @PutMapping("/update")
    @GuestApiDocs.UpdateGuest
    public ResponseEntity<GuestEntity> updateGuest(
            @RequestBody GuestRequest request) {

        log.info("BEGIN - Updating guest: {}, {}", request.getFirstName(), request.getLastName());

        GuestEntity updatedGuest = guestService.updateGuest(request);

        log.info("END - Guest updated successfully");

        return ResponseEntity.ok(updatedGuest);
    }

    /**
     * Delete guest
     * @param id Guest ID to delete
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @GuestApiDocs.DeleteGuest
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        log.info("BEGIN - Deleting guest with ID: {}", id);

        guestService.removeGuest(id);

        log.info("END - Guest deleted successfully");

        return ResponseEntity.noContent().build();
    }
}
package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.GuestApiDocs;
import com.wedding.backend.wedding_app.entity.Guest;
import com.wedding.backend.wedding_app.service.GuestService;
import com.wedding.model.request.GuestRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/guests")
@Tag(name = "Guest Management", description = "APIs for managing wedding guests")
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    @GuestApiDocs.CreateGuest
    public ResponseEntity<Guest> createGuest(@RequestBody GuestRequest request) {
        log.info("BEGIN - Received request to create guest: {}", request);

        Guest guest = guestService.addGuest(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.isPlusOneAllowed()
        );

        log.info("END - Guest created and saved");

        return ResponseEntity.status(CREATED).body(guest);
    }

    @GetMapping("/search")
    @GuestApiDocs.FindGuestByName
    public ResponseEntity<?> findGuestByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        log.info("BEGIN - Searching for guest: {} {}", firstName, lastName);

        Guest guest = guestService.findGuestByName(firstName, lastName);

        log.info("END - Found guest");

        return ResponseEntity.status(OK).body(guest);
    }

    @GetMapping
    @GuestApiDocs.GetAllGuests
    public ResponseEntity<List<Guest>> getAllGuests() {

        log.info("BEGIN - Fetching all guests");

        List<Guest> allGuest = guestService.getAllGuests();

        log.info("END - {} guests found", allGuest.size());

        return ResponseEntity.status(OK).body(allGuest);
    }

    @PutMapping("/{id}")
    @GuestApiDocs.UpdateGuest
    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long id,
            @RequestBody GuestRequest request) {

        log.info("BEGIN - Updating guest ID = {}", id);

        Guest updatedGuest = guestService.updateGuest(request);

        log.info("END - Guest updated successfully");

        return ResponseEntity.ok(updatedGuest);
    }

    @DeleteMapping("/{id}")
    @GuestApiDocs.DeleteGuest
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        log.info("BEGIN - Deleting guest with ID: {}", id);

        guestService.removeGuest(id);

        log.info("END - Guest deleted successfully");

        return ResponseEntity.noContent().build();
    }
}

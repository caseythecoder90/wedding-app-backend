package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.entity.Guest;
import com.wedding.backend.wedding_app.service.GuestService;
import com.wedding.model.request.GuestRequest;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/guests")
public class GuestController {

    private final GuestService guestService;
    private static final Logger log = LoggerFactory.getLogger(GuestController.class);

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
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

        return ResponseEntity.status(HttpStatus.CREATED).body(guest);
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> findGuestByName(
//            @RequestParam String firstName,
//            @RequestParam String lastName) {
//
//        log.info("Searching for guest: {} {}", firstName, lastName);
//
//        return guestService.findGuestByName(firstName, lastName)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Guest not found with name: " + firstName + " " + lastName));
//    }

    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        log.info("Fetching all guests");
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long id,
            @RequestBody GuestRequest request) {

        Guest updatedGuest = guestService.updateGuest(request);

        return ResponseEntity.ok(updatedGuest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        log.info("Deleting guest with ID: {}", id);

        try {
            guestService.removeGuest(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

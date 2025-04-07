package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.RSVPApiDocs;
import com.wedding.backend.wedding_app.dto.RSVPRequestDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.service.RSVPService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/rsvps")
@Tag(name = "RSVP Management", description = "APIs for managing wedding RSVPs")
public class RSVPController {

    private final RSVPService rsvpService;
    private final Logger log = LoggerFactory.getLogger(RSVPController.class);
    
    public RSVPController(RSVPService rsvpService) {
        this.rsvpService = rsvpService;
    }

    @GetMapping("/{guestId}")
    @RSVPApiDocs.GetRSVPByGuestId
    public ResponseEntity<RSVPResponseDTO> getRSVPByGuestId(@PathVariable Long guestId) {
        log.info("BEGIN - Fetching RSVP for guest ID: {}", guestId);
        
        try {
            RSVPResponseDTO rsvp = rsvpService.getRSVPByGuestId(guestId);
            log.info("END - RSVP found for guest ID: {}", guestId);
            return ResponseEntity.ok(rsvp);
        } catch (Exception e) {
            log.info("END - No RSVP found for guest ID: {}", guestId);
            throw e;
        }
    }

    @PostMapping
    @RSVPApiDocs.SubmitOrUpdateRSVP
    public ResponseEntity<RSVPResponseDTO> submitOrUpdateRSVP(@RequestBody RSVPRequestDTO request) {
        log.info("BEGIN - Processing RSVP submission for guest ID: {}", request.getGuestId());
        
        boolean exists = rsvpService.hasRSVP(request.getGuestId());
        RSVPResponseDTO response = rsvpService.submitOrUpdateRSVP(request);
        
        log.info("END - RSVP {} for guest ID: {}", exists ? "updated" : "created", request.getGuestId());
        return ResponseEntity.status(exists ? HttpStatus.OK : HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @RSVPApiDocs.DeleteRSVP
    public ResponseEntity<Void> deleteRSVP(@PathVariable Long id) {
        log.info("BEGIN - Deleting RSVP with ID: {}", id);
        
        rsvpService.deleteRSVP(id);
        
        log.info("END - RSVP deleted successfully");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RSVPApiDocs.GetAllRSVPs
    public ResponseEntity<List<RSVPResponseDTO>> getAllRSVPs() {
        log.info("BEGIN - Fetching all RSVPs");
        
        List<RSVPResponseDTO> rsvps = rsvpService.getAllRSVPs();
        
        log.info("END - Retrieved {} RSVPs", rsvps.size());
        return ResponseEntity.ok(rsvps);
    }
}

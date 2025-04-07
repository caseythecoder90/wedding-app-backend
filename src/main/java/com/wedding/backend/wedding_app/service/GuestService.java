package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.dto.GuestResponseDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.model.request.GuestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestDao guestDao;
    private final Logger log = LoggerFactory.getLogger(GuestService.class);
    
    public GuestService(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    /**
     * Add a new guest
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @param email Guest's email
     * @param phone Guest's phone
     * @param plusOneAllowed Whether guest can bring a plus one
     * @return Created guest entity
     */
    public GuestEntity addGuest(String firstName, String lastName, String email, String phone, boolean plusOneAllowed) {
        log.info("STARTED - Adding new guest: {} {}", firstName, lastName);
        
        // Check if guest already exists (the DAO will handle this, but we can catch it early)
        Optional<GuestEntity> existingGuest = guestDao.findGuestByFullName(firstName, lastName);
        if (existingGuest.isPresent()) {
            log.info("COMPLETED - Guest already exists with ID: {}", existingGuest.get().getId());
            return existingGuest.get();
        }
        
        GuestEntity savedGuest = guestDao.saveGuest(firstName, lastName, email, phone, plusOneAllowed);
        log.info("COMPLETED - New guest added with ID: {}", savedGuest.getId());
        
        return savedGuest;
    }

    /**
     * Find guest by name
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @return Guest entity if found
     * @throws WeddingAppException if guest not found
     */
    public GuestEntity findGuestByName(String firstName, String lastName) {
        log.info("STARTED - Finding guest by name: {} {}", firstName, lastName);
        
        GuestEntity guest = guestDao.findGuestByFullName(firstName, lastName)
                .orElseThrow(() -> WeddingAppException.guestNameNotFound(firstName, lastName));
        
        log.info("COMPLETED - Found guest with ID: {}", guest.getId());
        return guest;
    }

    /**
     * Find guest by ID
     * @param id Guest ID
     * @return Guest entity if found
     * @throws WeddingAppException if guest not found
     */
    public GuestEntity findById(Long id) {
        log.info("STARTED - Finding guest by ID: {}", id);
        
        GuestEntity guest = guestDao.findGuestById(id)
                .orElseThrow(() -> WeddingAppException.guestNotFound(id));
        
        log.info("COMPLETED - Found guest: {} {}", guest.getFirstName(), guest.getLastName());
        return guest;
    }

    /**
     * Get verified guest with DTO response
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @return Guest response DTO
     * @throws WeddingAppException if guest not found
     */
    public GuestResponseDTO getVerifiedGuest(String firstName, String lastName) {
        log.info("STARTED - Verifying guest: {} {}", firstName, lastName);
        
        GuestResponseDTO responseDTO = guestDao.findGuestByFullName(firstName, lastName)
                .map(this::mapToGuestResponseDTO)
                .orElseThrow(() -> WeddingAppException.guestNameNotFound(firstName, lastName));
        
        log.info("COMPLETED - Guest verified successfully with ID: {}", responseDTO.getId());
        return responseDTO;
    }

    /**
     * Get all guests
     * @return List of all guest entities
     */
    public List<GuestEntity> getAllGuests() {
        log.info("STARTED - Fetching all guests");
        
        List<GuestEntity> guests = guestDao.findAllGuests();
        
        log.info("COMPLETED - Found {} guests", guests.size());
        return guests;
    }

    /**
     * Update existing guest
     * @param request Guest request with updated details
     * @return Updated guest entity
     */
    public GuestEntity updateGuest(GuestRequest request) {
        log.info("STARTED - Updating guest: {} {}", request.getFirstName(), request.getLastName());
        
        // Find the guest to update
        GuestEntity existingGuest = findGuestByName(request.getFirstName(), request.getLastName());
        
        // Update fields
        existingGuest.setEmail(request.getEmail());
        existingGuest.setPhone(request.getPhone());
        existingGuest.setPlusOneAllowed(request.isPlusOneAllowed());
        
        // Save updates
        GuestEntity updatedGuest = guestDao.updateGuest(existingGuest);
        
        log.info("COMPLETED - Guest updated successfully with ID: {}", updatedGuest.getId());
        return updatedGuest;
    }

    /**
     * Remove guest by ID
     * @param id Guest ID to remove
     */
    public void removeGuest(Long id) {
        log.info("STARTED - Removing guest with ID: {}", id);
        
        guestDao.deleteGuest(id);
        
        log.info("COMPLETED - Guest removed successfully");
    }

    /**
     * Map guest entity to response DTO
     * @param guest Guest entity
     * @return Guest response DTO
     */
    private GuestResponseDTO mapToGuestResponseDTO(GuestEntity guest) {
        return GuestResponseDTO.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .plusOneAllowed(guest.getPlusOneAllowed())
                .hasRsvp(guest.getRsvp() != null)
                .rsvpId(guest.getRsvp() != null ? guest.getRsvp().getId() : null)
                .build();
    }
}
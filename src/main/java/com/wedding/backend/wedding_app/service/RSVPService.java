package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.RSVPDao;
import com.wedding.backend.wedding_app.dto.RSVPRequestDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class RSVPService {

    private final RSVPDao rsvpDao;
    private final GuestRepository guestRepository;
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(RSVPService.class);
    
    public RSVPService(RSVPDao rsvpDao, GuestRepository guestRepository, EmailService emailService) {
        this.rsvpDao = rsvpDao;
        this.guestRepository = guestRepository;
        this.emailService = emailService;
    }

    /**
     * Get RSVP by ID
     * @param id RSVP ID
     * @return RSVP response DTO
     */
    public RSVPResponseDTO getRSVPById(Long id) {
        log.info("STARTED - Getting RSVP with ID: {}", id);
        
        RSVPEntity rsvp = rsvpDao.findRSVPById(id)
                .orElseThrow(() -> WeddingAppException.rsvpNotFound(id));
        
        RSVPResponseDTO responseDTO = mapToRSVPResponseDTO(rsvp);
        log.info("COMPLETED - Found RSVP for guest: {}", responseDTO.getGuestName());
        
        return responseDTO;
    }

    /**
     * Get RSVP by guest ID
     * @param guestId Guest ID
     * @return RSVP response DTO
     */
    public RSVPResponseDTO getRSVPByGuestId(Long guestId) {
        log.info("STARTED - Getting RSVP for guest ID: {}", guestId);
        
        Optional<RSVPEntity> rsvpOpt = rsvpDao.findRSVPByGuestId(guestId);
        
        if (rsvpOpt.isEmpty()) {
            log.info("COMPLETED - No RSVP found for guest ID: {}", guestId);
            throw WeddingAppException.rsvpNotFound(guestId);
        }
        
        RSVPResponseDTO responseDTO = mapToRSVPResponseDTO(rsvpOpt.get());
        log.info("COMPLETED - Found RSVP for guest: {}", responseDTO.getGuestName());
        
        return responseDTO;
    }

    /**
     * Check if guest has an RSVP
     * @param guestId Guest ID
     * @return true if guest has RSVP, false otherwise
     */
    public boolean hasRSVP(Long guestId) {
        log.info("STARTED - Checking if guest ID: {} has RSVP", guestId);
        
        boolean hasRsvp = rsvpDao.findRSVPByGuestId(guestId).isPresent();
        
        log.info("COMPLETED - Guest ID: {} has RSVP: {}", guestId, hasRsvp);
        return hasRsvp;
    }

    /**
     * Get all RSVPs
     * @return List of RSVP response DTOs
     */
    public List<RSVPResponseDTO> getAllRSVPs() {
        log.info("STARTED - Getting all RSVPs");
        
        List<RSVPResponseDTO> rsvps = rsvpDao.findAllRSVPs().stream()
                .map(this::mapToRSVPResponseDTO)
                .collect(Collectors.toList());
        
        log.info("COMPLETED - Found {} RSVPs", rsvps.size());
        return rsvps;
    }

    /**
     * Submit or update an RSVP
     * @param request RSVP request DTO
     * @return RSVP response DTO
     */
    public RSVPResponseDTO submitOrUpdateRSVP(RSVPRequestDTO request) {
        log.info("STARTED - Processing RSVP for guest ID: {}", request.getGuestId());
        
        // Validate the guest exists
        GuestEntity guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> WeddingAppException.guestNotFound(request.getGuestId()));
        
        // If guest is bringing a plus one, validate they're allowed to
        if (Boolean.TRUE.equals(request.getBringingPlusOne()) && !guest.getPlusOneAllowed()) {
            log.warn("Guest ID: {} attempted to add plus-one but is not allowed", request.getGuestId());
            throw WeddingAppException.invalidParameter("bringingPlusOne");
        }

        // If not bringing a plus one, clear the plus one name
        String plusOneName = Boolean.TRUE.equals(request.getBringingPlusOne()) 
                ? request.getPlusOneName() 
                : null;

        // Save the RSVP
        RSVPEntity savedRSVP = rsvpDao.saveRSVP(
                request.getGuestId(),
                request.getAttending(),
                request.getBringingPlusOne(),
                plusOneName,
                request.getDietaryRestrictions());

        // If email was provided in the request, update the guest email
        if (StringUtils.isNotBlank(request.getEmail())) {
            log.info("Updating email for guest ID: {}", request.getGuestId());
            guest.setEmail(request.getEmail());
            guestRepository.save(guest);
        }
        
        // Send email confirmation if requested and email is available
        if (request.isSendConfirmationEmail() && StringUtils.isNotBlank(guest.getEmail())) {
            try {
                log.info("Sending RSVP confirmation email to guest: {}", guest.getEmail());
                emailService.sendRSVPConfirmationEmail(savedRSVP, guest);
            } catch (Exception e) {
                // Log the error but don't fail the RSVP process
                log.error("Failed to send RSVP confirmation email", e);
            }
        }
        
        RSVPResponseDTO responseDTO = mapToRSVPResponseDTO(savedRSVP);

        // this logic is wrong need to fix
        boolean isNewRsvp = savedRSVP.getId() != null && savedRSVP.getId() == 0;
        log.info("COMPLETED - RSVP {} for guest: {}", isNewRsvp ? "created" : "updated", responseDTO.getGuestName());

        return responseDTO;
    }

    /**
     * Delete an RSVP
     * @param id RSVP ID
     */
    public void deleteRSVP(Long id) {
        log.info("STARTED - Deleting RSVP with ID: {}", id);
        
        rsvpDao.deleteRSVP(id);
        
        log.info("COMPLETED - RSVP deleted successfully");
    }

    /**
     * Delete RSVP by guest ID
     * @param guestId Guest ID
     */
    public void deleteRSVPByGuestId(Long guestId) {
        log.info("STARTED - Deleting RSVP for guest ID: {}", guestId);
        
        rsvpDao.deleteRSVPByGuestId(guestId);
        
        log.info("COMPLETED - RSVP for guest ID: {} deleted successfully", guestId);
    }

    /**
     * Map RSVP entity to response DTO
     * @param rsvp RSVP entity
     * @return RSVP response DTO
     */
    private RSVPResponseDTO mapToRSVPResponseDTO(RSVPEntity rsvp) {
        GuestEntity guest = rsvp.getGuest();
        String guestName = guest != null 
                ? guest.getFirstName() + " " + guest.getLastName() 
                : "Unknown Guest";
                
        return RSVPResponseDTO.builder()
                .id(rsvp.getId())
                .guestId(guest != null ? guest.getId() : null)
                .guestName(guestName)
                .attending(rsvp.getAttending())
                .bringingPlusOne(rsvp.getBringingPlusOne())
                .plusOneName(rsvp.getPlusOneName())
                .dietaryRestrictions(rsvp.getDietaryRestrictions())
                .submittedAt(rsvp.getSubmittedAt())
                .build();
    }
}
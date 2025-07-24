package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import com.wedding.backend.wedding_app.repository.RSVPRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class RSVPDao {

    private final RSVPRespository rsvpRepository;
    private final GuestRepository guestRepository;
    private final Logger log = LoggerFactory.getLogger(RSVPDao.class);
    
    public RSVPDao(RSVPRespository rsvpRepository, GuestRepository guestRepository) {
        this.rsvpRepository = rsvpRepository;
        this.guestRepository = guestRepository;
    }

    /**
     * Find RSVP by ID
     * @param id The RSVP ID
     * @return Optional RSVP entity
     */
    @Transactional(readOnly = true)
    public Optional<RSVPEntity> findRSVPById(Long id) {
        try {
            return rsvpRepository.findById(id);
        } catch (Exception e) {
            log.error("Error finding RSVP with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find RSVP by guest ID
     * @param guestId The guest ID
     * @return Optional RSVP entity
     */
    @Transactional(readOnly = true)
    public Optional<RSVPEntity> findRSVPByGuestId(Long guestId) {
        try {
            Optional<GuestEntity> guestOpt = guestRepository.findById(guestId);
            if (guestOpt.isEmpty()) {
                log.warn("Guest not found with ID: {}", guestId);
                return Optional.empty();
            }
            
            GuestEntity guest = guestOpt.get();
            return Optional.ofNullable(guest.getRsvp());
        } catch (Exception e) {
            log.error("Error finding RSVP for guest ID: {}", guestId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all RSVPs
     * @return List of all RSVP entities
     */
    @Transactional(readOnly = true)
    public List<RSVPEntity> findAllRSVPs() {
        try {
            return rsvpRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all RSVPs", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Create or update an RSVP for a guest
     * @param guestId Guest ID
     * @param attending Whether guest is attending
     * @param bringingPlusOne Whether guest is bringing a plus one
     * @param plusOneName Name of the plus one
     * @param dietaryRestrictions Dietary restrictions
     * @return Created or updated RSVP entity
     */
    @Transactional
    public RSVPEntity saveRSVP(Long guestId, Boolean attending, Boolean bringingPlusOne, 
                             String plusOneName, String dietaryRestrictions) {
        try {
            GuestEntity guest = guestRepository.findById(guestId)
                    .orElseThrow(() -> WeddingAppException.guestNotFound(guestId));

            RSVPEntity rsvp = guest.getRsvp();
            boolean isNewRSVP = rsvp == null;

            log.info("{} RSVP for guest ID: {}", isNewRSVP ? "Creating new" : "Updating existing", guestId);

            if (isNewRSVP) {
                rsvp = new RSVPEntity();
                rsvp.setGuest(guest);
                rsvp.setSubmittedAt(OffsetDateTime.now());
            }

            rsvp.setAttending(attending);
            rsvp.setBringingPlusOne(bringingPlusOne);
            rsvp.setPlusOneName(plusOneName);
            rsvp.setDietaryRestrictions(dietaryRestrictions);

            RSVPEntity savedRSVP = rsvpRepository.save(rsvp);
            
            // Update the guest's reference to the RSVP
            guest.setRsvp(savedRSVP);
            guestRepository.save(guest);
            
            return savedRSVP;
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error saving RSVP for guest ID: {}", guestId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Update an existing RSVP
     * @param rsvp The RSVP entity to update
     * @return Updated RSVP entity
     */
    @Transactional
    public RSVPEntity updateRSVP(RSVPEntity rsvp) {
        try {
            // Ensure the RSVP exists
            if (rsvp.getId() == null || !rsvpRepository.existsById(rsvp.getId())) {
                throw WeddingAppException.rsvpNotFound(rsvp.getId());
            }
            
            return rsvpRepository.save(rsvp);
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error updating RSVP ID: {}", rsvp.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete an RSVP by ID
     * @param id The RSVP ID to delete
     */
    @Transactional
    public void deleteRSVP(Long id) {
        try {
            Optional<RSVPEntity> rsvpOpt = rsvpRepository.findById(id);
            if (rsvpOpt.isEmpty()) {
                log.warn("RSVP not found with ID: {}", id);
                return;
            }
            
            RSVPEntity rsvp = rsvpOpt.get();
            GuestEntity guest = rsvp.getGuest();
            
            // Remove the reference from guest
            if (guest != null) {
                guest.setRsvp(null);
                guestRepository.save(guest);
            }
            
            // Now delete the RSVP
            rsvpRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting RSVP with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete RSVP by guest ID
     * @param guestId The guest ID whose RSVP should be deleted
     */
    @Transactional
    public void deleteRSVPByGuestId(Long guestId) {
        try {
            Optional<GuestEntity> guestOpt = guestRepository.findById(guestId);
            if (guestOpt.isEmpty()) {
                log.warn("Guest not found with ID: {}", guestId);
                return;
            }
            
            GuestEntity guest = guestOpt.get();
            RSVPEntity rsvp = guest.getRsvp();
            
            if (rsvp != null) {
                // Remove the reference from guest
                guest.setRsvp(null);
                guestRepository.save(guest);
                
                // Now delete the RSVP
                rsvpRepository.delete(rsvp);
            }
        } catch (Exception e) {
            log.error("Error deleting RSVP for guest ID: {}", guestId, e);
            throw WeddingAppException.databaseError();
        }
    }
}
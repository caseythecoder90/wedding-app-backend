package dao;

import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.InvitationCodeEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import com.wedding.backend.wedding_app.repository.InvitationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class InvitationDao {

    private static final Logger log = LoggerFactory.getLogger(InvitationDao.class);
    
    private final InvitationCodeRepository invitationCodeRepository;
    private final GuestRepository guestRepository;
    
    public InvitationDao(InvitationCodeRepository invitationCodeRepository, GuestRepository guestRepository) {
        this.invitationCodeRepository = invitationCodeRepository;
        this.guestRepository = guestRepository;
    }
    
    /**
     * Find invitation code by code string
     * @param code The invitation code string
     * @return Optional invitation code entity
     */
    @Transactional(readOnly = true)
    public Optional<InvitationCodeEntity> findInvitationByCode(String code) {
        try {
            return invitationCodeRepository.findByCode(code);
        } catch (Exception e) {
            log.error("Error finding invitation code: {}", code, e);
            throw WeddingAppException.databaseError();
        }
    }
    
    /**
     * Check if a code already exists
     * @param code The code to check
     * @return true if code exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        try {
            return invitationCodeRepository.findByCode(code).isPresent();
        } catch (Exception e) {
            log.error("Error checking if code exists: {}", code, e);
            throw WeddingAppException.databaseError();
        }
    }
    
    /**
     * Find all invitation codes for a guest
     * @param guestId The guest ID
     * @return List of invitation code entities
     */
    @Transactional(readOnly = true)
    public List<InvitationCodeEntity> findInvitationsByGuestId(Long guestId) {
        try {
            Optional<GuestEntity> guestOpt = guestRepository.findById(guestId);
            if (guestOpt.isEmpty()) {
                log.warn("Guest not found with ID: {}", guestId);
                throw WeddingAppException.guestNotFound(guestId);
            }
            
            GuestEntity guest = guestOpt.get();
            return invitationCodeRepository.findByGuest(guest);
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error finding invitation codes for guest ID: {}", guestId, e);
            throw WeddingAppException.databaseError();
        }
    }
    
    /**
     * Save a new invitation code entity
     * @param invitationCode The invitation code entity to save
     * @return Saved invitation code entity
     */
    @Transactional
    public InvitationCodeEntity saveInvitationCode(InvitationCodeEntity invitationCode) {
        log.info("Saving invitation code for guest ID: {}", 
                invitationCode.getGuest() != null ? invitationCode.getGuest().getId() : "null");
        
        try {
            InvitationCodeEntity savedCode = invitationCodeRepository.save(invitationCode);
            log.info("Saved invitation code: {}", savedCode.getCode());
            return savedCode;
        } catch (Exception e) {
            log.error("Error saving invitation code", e);
            throw WeddingAppException.databaseError();
        }
    }
    
    /**
     * Update an invitation code entity
     * @param invitationCode The invitation code entity to update
     * @return Updated invitation code entity
     */
    @Transactional
    public InvitationCodeEntity updateInvitationCode(InvitationCodeEntity invitationCode) {
        log.info("Updating invitation code: {}", invitationCode.getCode());
        
        try {
            // Ensure the invitation code exists
            if (invitationCode.getId() == null || 
                !invitationCodeRepository.existsById(invitationCode.getId())) {
                throw WeddingAppException.invalidInvitationCode(invitationCode.getCode());
            }
            
            InvitationCodeEntity updatedCode = invitationCodeRepository.save(invitationCode);
            log.info("Invitation code updated successfully: {}", updatedCode.getCode());
            return updatedCode;
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error updating invitation code ID: {}", invitationCode.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete invitation code
     * @param id The invitation code ID
     */
    @Transactional
    public void deleteInvitationCode(Long id) {
        log.info("Deleting invitation code with ID: {}", id);
        
        try {
            if (!invitationCodeRepository.existsById(id)) {
                log.warn("No invitation code found with ID: {} to delete", id);
                return;
            }
            
            invitationCodeRepository.deleteById(id);
            log.info("Invitation code with ID: {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error deleting invitation code with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }
}
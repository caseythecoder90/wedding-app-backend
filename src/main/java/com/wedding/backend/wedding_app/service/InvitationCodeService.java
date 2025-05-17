package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.dao.InvitationDao;
import com.wedding.backend.wedding_app.dao.RSVPDao;
import com.wedding.backend.wedding_app.dto.GuestResponseDTO;
import com.wedding.backend.wedding_app.dto.InvitationValidationResponseDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.InvitationCodeEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CHARSET;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CODE_LENGTH;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CODE_PREFIX;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.SPACE;

@Service
public class InvitationCodeService {

    private static final Logger log = LoggerFactory.getLogger(InvitationCodeService.class);

    private final InvitationDao invitationDao;
    private final GuestDao guestDao;
    private final RSVPDao rsvpDao;

    @Autowired
    public InvitationCodeService(InvitationDao invitationDao, GuestDao guestDao, RSVPDao rsvpDao) {
        this.invitationDao = invitationDao;
        this.guestDao = guestDao;
        this.rsvpDao = rsvpDao;
    }

    /**
     * Generates a unique random alphanumeric code
     * Format: WED-XXXXXX where X is an alphanumeric character
     */
    public String generateUniqueCode() {
        log.info("BEGIN - Generating unique invitation code");

        StringBuilder sb = new StringBuilder(CODE_PREFIX + "-");
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(randomIndex));
        }

        String code = sb.toString();

        // Check if code already exists
        if (invitationDao.codeExists(code)) {
            log.info("Code collision detected, regenerating");
            return generateUniqueCode(); // recursively try again
        }

        log.info("END - Generated unique code: {}", code);
        return code;
    }

    /**
     * Creates a new invitation code for a guest
     * @param guestId The guest ID
     * @param codeType The type of code (PRIMARY, REPLACEMENT, etc.)
     * @return The created invitation code entity
     */
    public InvitationCodeEntity createInvitationCode(Long guestId, String codeType) {
        log.info("BEGIN - Creating invitation code for guest ID: {}, type: {}", guestId, codeType);

        Optional<GuestEntity> guestOpt = guestDao.findGuestById(guestId);
        if (guestOpt.isEmpty()) {
            log.error("Guest not found with ID: {}", guestId);
            throw WeddingAppException.guestNotFound(guestId);
        }

        try {
            String code = generateUniqueCode();
            GuestEntity guest = guestOpt.get();

            InvitationCodeEntity invitationCode = InvitationCodeEntity.builder()
                    .code(code)
                    .guest(guest)
                    .createdDate(LocalDateTime.now())
                    .expiryDate(LocalDateTime.now().plusYears(3L))
                    .used(false)
                    .codeType(codeType)
                    .build();

            InvitationCodeEntity savedCode = invitationDao.saveInvitationCode(invitationCode);
            log.info("END - Created invitation code: {}", savedCode.getCode());

            return savedCode;
        } catch (Exception e) {
            log.error("Error creating invitation code for guest ID: {}", guestId, e);
            throw WeddingAppException.invitationCodeCreationError(guestId);
        }
    }

    /**
     * Validates a code and returns the associated guest if valid
     * @param code The invitation code to validate
     * @return The guest associated with the code
     * @throws WeddingAppException if code is invalid or expired
     */
    public GuestEntity validateCode(String code) {
        log.info("BEGIN - Validating invitation code: {}", code);

        Optional<InvitationCodeEntity> invCodeOpt = invitationDao.findInvitationByCode(code);

        if (invCodeOpt.isEmpty()) {
            log.warn("Invalid invitation code: {}", code);
            throw WeddingAppException.invalidInvitationCode(code);
        }

        InvitationCodeEntity invCode = invCodeOpt.get();

        // Check if expired
        if (invCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Expired invitation code: {}", code);
            throw WeddingAppException.expiredInvitationCode(code);
        }

        log.info("END - Validated invitation code for guest: {}", invCode.getGuest().getId());
        return invCode.getGuest();
    }

    /**
     * Validates an invitation code and returns a DTO with guest and RSVP information (if exists)
     * @param code The invitation code to validate
     * @return InvitationValidationResponseDTO containing guest and RSVP information
     * @throws WeddingAppException if code is invalid or expired
     */
    public InvitationValidationResponseDTO validateInvitationAndRetrieveRSVP(String code) {
        log.info("BEGIN - Validating invitation code and retrieving RSVP data: {}", code);

        // Validate the code and get the guest
        GuestEntity guest = validateCode(code);

        // Map guest to DTO
        GuestResponseDTO guestResponseDTO = GuestResponseDTO.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .plusOneAllowed(guest.getPlusOneAllowed())
                .hasRsvp(guest.getRsvp() != null)
                .rsvpId(guest.getRsvp() != null ? guest.getRsvp().getId() : null)
                .build();

        // Check if guest has an RSVP
        RSVPResponseDTO rsvpResponseDTO = null;
        boolean hasExistingRsvp = false;

        if (guest.getRsvp() != null) {
            log.info("Guest has an existing RSVP, retrieving RSVP data");
            Optional<RSVPEntity> rsvpOpt = rsvpDao.findRSVPById(guest.getRsvp().getId());

            if (rsvpOpt.isPresent()) {
                RSVPEntity rsvp = rsvpOpt.get();
                hasExistingRsvp = true;

                // Map to DTO
                rsvpResponseDTO = RSVPResponseDTO.builder()
                        .id(rsvp.getId())
                        .guestId(guest.getId())
                        .guestName(guest.getFirstName() + SPACE + guest.getLastName())
                        .attending(rsvp.getAttending())
                        .bringingPlusOne(rsvp.getBringingPlusOne())
                        .plusOneName(rsvp.getPlusOneName())
                        .dietaryRestrictions(rsvp.getDietaryRestrictions())
                        .submittedAt(rsvp.getSubmittedAt())
                        .build();
            }
        }

        // Build the response
        InvitationValidationResponseDTO responseDTO = InvitationValidationResponseDTO.builder()
                .guest(guestResponseDTO)
                .existingRsvp(rsvpResponseDTO)
                .hasExistingRsvp(hasExistingRsvp)
                .build();

        log.info("END - Validated invitation code and retrieved RSVP data for guest ID: {}", guest.getId());
        return responseDTO;
    }

    /**
     * Gets all invitation codes for a guest
     * @param guestId The guest ID
     * @return List of invitation codes
     */
    public List<InvitationCodeEntity> getCodesForGuest(Long guestId) {
        log.info("BEGIN - Getting invitation codes for guest ID: {}", guestId);

        List<InvitationCodeEntity> codes = invitationDao.findInvitationsByGuestId(guestId);
        log.info("END - Found {} invitation codes for guest ID: {}", codes.size(), guestId);

        return codes;
    }

    /**
     * Marks a code as used
     * @param code The code to mark as used
     */
    public void markCodeAsUsed(String code) {
        log.info("BEGIN - Marking invitation code as used: {}", code);

        Optional<InvitationCodeEntity> invCodeOpt = invitationDao.findInvitationByCode(code);
        if (invCodeOpt.isEmpty()) {
            log.warn("Cannot mark as used - code not found: {}", code);
            throw WeddingAppException.invalidInvitationCode(code);
        }

        InvitationCodeEntity invCode = invCodeOpt.get();
        invCode.setUsed(true);
        invitationDao.updateInvitationCode(invCode);
        log.info("END - Marked invitation code as used: {}", code);
    }

    /**
     * Generates a replacement code for a guest
     * @param guestId The guest ID
     * @return The new invitation code
     */
    public InvitationCodeEntity generateReplacementCode(Long guestId) {
        log.info("BEGIN - Generating replacement code for guest ID: {}", guestId);
        return createInvitationCode(guestId, "REPLACEMENT");
    }

    /**
     * Invalidates all codes for a guest
     * @param guestId The guest ID
     */
    public void invalidateAllCodesForGuest(Long guestId) {
        log.info("BEGIN - Invalidating all codes for guest ID: {}", guestId);

        List<InvitationCodeEntity> codes = invitationDao.findInvitationsByGuestId(guestId);

        for (InvitationCodeEntity code : codes) {
            code.setExpiryDate(LocalDateTime.now().minusDays(1)); // Set to expired
            code.setUsed(true);
            invitationDao.updateInvitationCode(code);
        }

        log.info("END - Invalidated {} codes for guest ID: {}", codes.size(), guestId);
    }
}
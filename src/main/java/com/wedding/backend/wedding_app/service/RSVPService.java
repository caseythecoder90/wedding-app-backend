package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.config.EmailConfig;
import com.wedding.backend.wedding_app.dao.RSVPDao;
import com.wedding.backend.wedding_app.dto.RSVPRequestDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.dto.RSVPSummaryDTO;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class RSVPService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

    private final RSVPDao rsvpDao;
    private final GuestRepository guestRepository;
    private final EmailService emailService;
    private final EmailConfig emailConfig;
    private final Logger log = LoggerFactory.getLogger(RSVPService.class);

    public RSVPService(RSVPDao rsvpDao, GuestRepository guestRepository,
                      EmailService emailService, EmailConfig emailConfig) {
        this.rsvpDao = rsvpDao;
        this.guestRepository = guestRepository;
        this.emailService = emailService;
        this.emailConfig = emailConfig;
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
                .toList();
        
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

        // Check if this is a new RSVP or an update
        boolean isExistingRsvp = rsvpDao.findRSVPByGuestId(request.getGuestId()).isPresent();
        String action = isExistingRsvp ? "updating" : "creating";
        log.info("{} RSVP for guest ID: {}", action, request.getGuestId());

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
        
        // Handle email notifications asynchronously
        // 1. Start guest confirmation email if requested and email is available
        if (request.isSendConfirmationEmail() && StringUtils.isNotBlank(guest.getEmail())) {
            log.info("Initiating asynchronous guest confirmation email");
            sendGuestConfirmationEmailAsync(savedRSVP, guest);
        }

        // 2. Always send admin notification (if enabled) - independent of guest confirmation
        sendAdminNotificationEmailAsync(savedRSVP, guest);

        // No need to wait for async methods - they'll run in the background
        
        RSVPResponseDTO responseDTO = mapToRSVPResponseDTO(savedRSVP);

        log.info("COMPLETED - RSVP {} for guest: {}", isExistingRsvp ? "updated" : "created", responseDTO.getGuestName());

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
     * Send a confirmation email to the guest asynchronously
     * @param rsvpEntity The RSVP entity
     * @param guestEntity The guest entity
     */
    @Async
    public void sendGuestConfirmationEmailAsync(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        try {
            log.info("Asynchronously sending confirmation email to guest: {}", guestEntity.getEmail());
            emailService.sendRSVPConfirmationEmail(rsvpEntity, guestEntity);
            log.info("Successfully sent confirmation email to guest: {}", guestEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email to guest: {}", guestEntity.getEmail(), e);
            // Errors are caught and logged but not propagated
        }
    }

    /**
     * Send an admin notification email with RSVP summary asynchronously
     * @param rsvpEntity The RSVP entity that triggered the notification
     * @param guestEntity The guest entity
     */
    @Async
    public void sendAdminNotificationEmailAsync(RSVPEntity rsvpEntity, GuestEntity guestEntity) {
        try {
            // Check if admin notifications are enabled
            if (!emailConfig.isSendAdminNotifications() ||
                StringUtils.isBlank(emailConfig.getAdminEmail())) {
                log.info("Admin notifications are disabled or no admin email configured");
                return;
            }

            log.info("Asynchronously sending admin notification email");

            // Create RSVP summary
            RSVPSummaryDTO summary = buildRsvpSummary();

            // Send admin notification with full summary
            emailService.sendAdminRsvpNotification(rsvpEntity, guestEntity, summary);

            log.info("Successfully sent admin notification email");
        } catch (Exception e) {
            log.error("Failed to send admin notification email", e);
            // Errors are caught and logged but not propagated
        }
    }

    /**
     * Build a comprehensive summary of all RSVPs
     * @return An RSVPSummaryDTO containing all stats and lists
     */
    private RSVPSummaryDTO buildRsvpSummary() {
        log.info("Building RSVP summary for admin notification");

        try {
            // Get all RSVPs
            List<RSVPResponseDTO> allRsvps = getAllRSVPs();

            // Calculate summary statistics
            int totalRsvps = allRsvps.size();
            long totalAttending = allRsvps.stream()
                    .filter(rsvp -> Boolean.TRUE.equals(rsvp.getAttending()))
                    .count();
            long totalNotAttending = totalRsvps - totalAttending;

            // Calculate total guests including plus ones
            long totalGuests = totalAttending + allRsvps.stream()
                    .filter(rsvp -> Boolean.TRUE.equals(rsvp.getAttending()) &&
                           Boolean.TRUE.equals(rsvp.getBringingPlusOne()))
                    .count();

            // Split into attending and not attending lists
            List<RSVPResponseDTO> attendingRsvps = allRsvps.stream()
                    .filter(rsvp -> Boolean.TRUE.equals(rsvp.getAttending()))
                    .toList();

            List<RSVPResponseDTO> notAttendingRsvps = allRsvps.stream()
                    .filter(rsvp -> Boolean.FALSE.equals(rsvp.getAttending()))
                    .toList();

            // Build and return the summary
            return RSVPSummaryDTO.builder()
                    .totalRsvps(totalRsvps)
                    .totalAttending(totalAttending)
                    .totalNotAttending(totalNotAttending)
                    .totalGuests(totalGuests)
                    .attendingRsvps(attendingRsvps)
                    .notAttendingRsvps(notAttendingRsvps)
                    .lastUpdated(LocalDateTime.now().format(DATE_FORMATTER))
                    .build();

        } catch (Exception e) {
            log.error("Error building RSVP summary", e);
            // Return an empty summary rather than letting the exception propagate
            return new RSVPSummaryDTO();
        }
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
                .guestEmail(guest != null ? guest.getEmail() : null)
                .attending(rsvp.getAttending())
                .bringingPlusOne(rsvp.getBringingPlusOne())
                .plusOneName(rsvp.getPlusOneName())
                .dietaryRestrictions(rsvp.getDietaryRestrictions())
                .submittedAt(rsvp.getSubmittedAt())
                .build();
    }
}
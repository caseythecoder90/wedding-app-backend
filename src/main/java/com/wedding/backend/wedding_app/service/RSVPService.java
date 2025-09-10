package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.dao.RSVPDao;
import com.wedding.backend.wedding_app.dto.RSVPRequestDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.dto.RSVPSummaryDTO;
import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.FamilyGroupRepository;
import com.wedding.backend.wedding_app.dao.FamilyMemberDao;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RSVPService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

    private final RSVPDao rsvpDao;
    private final GuestDao guestDao;
    private final GuestRepository guestRepository;
    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberDao familyMemberDao;
    private final FamilyMemberService familyMemberService;
    private final EmailService emailService;

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
        log.info("COMPLETED - Found RSVP for guest: {} [Used guestId]", responseDTO.getGuestName());
        
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
     * Submit or update an RSVP for all guest types (solo, plus-one, family)
     * @param request RSVP request DTO
     * @return RSVP response DTO
     */
    @Transactional
    public RSVPResponseDTO submitOrUpdateRSVP(RSVPRequestDTO request) {
        log.info("STARTED - Processing RSVP for guest ID: {}", request.getGuestId());

        // Validate the guest exists and eagerly load family members for async email processing
        GuestEntity guest = guestDao.findGuestByIdWithFamilyMembers(request.getGuestId())
                .orElseThrow(() -> WeddingAppException.guestNotFound(request.getGuestId()));


        // Save the primary guest RSVP
        RSVPEntity savedRSVP = rsvpDao.saveRSVP(
                request.getGuestId(),
                request.getAttending(),
                request.getDietaryRestrictions());

        // Process family member RSVPs - this handles both family groups and plus-ones as family members
        if (CollectionUtils.isNotEmpty(request.getFamilyMembers())) {
            log.info("Processing family member RSVPs for {} members", request.getFamilyMembers().size());
            
            FamilyGroupEntity familyGroup = guest.getFamilyGroup();
            if (familyGroup == null) {
                // For plus-one scenarios, create a temporary family group if needed
                if (guest.getPlusOneAllowed() && request.getFamilyMembers().size() == 1) {
                    log.info("Processing plus-one as family member for solo guest");
                    familyGroup = createTemporaryFamilyGroupForPlusOne(guest);
                } else {
                    throw WeddingAppException.invalidParameter("familyMembers - guest not in family group and not plus-one eligible");
                }
            }
            
            familyMemberService.processFamilyMemberRSVPs(request.getFamilyMembers(), familyGroup);
        }

        // If email was provided in the request, update the guest email
        if (StringUtils.isNotBlank(request.getEmail())) {
            log.info("Updating email for guest ID: {}", request.getGuestId());
            guest.setEmail(request.getEmail());
            guestDao.updateGuest(guest);
        }

        // Send emails using the already loaded guest (family members eagerly loaded)
        if (request.isSendConfirmationEmail() && StringUtils.isNotBlank(guest.getEmail())) {
            log.info("Initiating asynchronous guest confirmation email");
            emailService.sendGuestConfirmationEmailAsync(savedRSVP, guest);
        }

        RSVPSummaryDTO summary = buildRsvpSummary();
        emailService.sendAdminNotificationEmailAsync(savedRSVP, guest, summary);

        RSVPResponseDTO responseDTO = mapToRSVPResponseDTO(savedRSVP);

        log.info("COMPLETED - RSVP processed for guest: {}", responseDTO.getGuestName());

        return responseDTO;
    }

    /**
     * Delete an RSVP
     * @param id RSVP ID
     */
    @Transactional
    public void deleteRSVP(Long id) {
        log.info("STARTED - Deleting RSVP with ID: {}", id);
        
        // First get the RSVP to check if the guest has family members
        RSVPEntity rsvp = rsvpDao.findRSVPById(id)
                .orElseThrow(() -> WeddingAppException.rsvpNotFound(id));
        
        GuestEntity guest = rsvp.getGuest();
        
        // Reset family members' attendance if guest is part of a family group
        if (guest != null && guest.getFamilyGroup() != null) {
            log.info("Resetting family members' attendance for guest ID: {}", guest.getId());
            familyMemberService.resetAllFamilyMembersAttendance(guest.getFamilyGroup());
        }
        
        rsvpDao.deleteRSVP(id);
        
        log.info("COMPLETED - RSVP deleted successfully");
    }

    /**
     * Delete RSVP by guest ID
     * @param guestId Guest ID
     */
    @Transactional
    public void deleteRSVPByGuestId(Long guestId) {
        log.info("STARTED - Deleting RSVP for guest ID: {}", guestId);
        
        // First get the guest to check if they have family members
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> WeddingAppException.guestNotFound(guestId));
        
        // Reset family members' attendance if guest is part of a family group
        if (guest.getFamilyGroup() != null) {
            log.info("Resetting family members' attendance for guest ID: {}", guestId);
            familyMemberService.resetAllFamilyMembersAttendance(guest.getFamilyGroup());
        }
        
        rsvpDao.deleteRSVPByGuestId(guestId);
        
        log.info("COMPLETED - RSVP for guest ID: {} deleted successfully", guestId);
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

            // Get all attending family members once (filtered at DB level)
            List<FamilyMemberEntity> attendingFamilyMembers = familyMemberDao.findAllAttending();
            long totalFamilyMembers = attendingFamilyMembers.size();
                    
            long totalGuests = totalAttending + totalFamilyMembers;

            // Split into attending and not attending lists
            List<RSVPResponseDTO> attendingRsvps = allRsvps.stream()
                    .filter(rsvp -> Boolean.TRUE.equals(rsvp.getAttending()))
                    .toList();

            List<RSVPResponseDTO> notAttendingRsvps = allRsvps.stream()
                    .filter(rsvp -> Boolean.FALSE.equals(rsvp.getAttending()))
                    .toList();

            // Map the already-fetched attending family members for admin visibility                 
            List<Map<String, Object>> attendingFamilyMembersData = attendingFamilyMembers.stream()
                    .map(member -> {
                        Map<String, Object> memberData = new HashMap<>();
                        memberData.put("groupName", member.getFamilyGroup().getGroupName());
                        memberData.put("firstName", member.getFirstName());
                        memberData.put("lastName", member.getLastName());
                        memberData.put("ageGroup", member.getAgeGroup());
                        memberData.put("dietaryRestrictions", member.getDietaryRestrictions());
                        return memberData;
                    })
                    .collect(Collectors.toList());
                    
            log.info("Found {} attending family members for admin summary", attendingFamilyMembersData.size());

            // Build and return the summary
            return RSVPSummaryDTO.builder()
                    .totalRsvps(totalRsvps)
                    .totalAttending(totalAttending)
                    .totalNotAttending(totalNotAttending)
                    .totalGuests(totalGuests)
                    .attendingRsvps(attendingRsvps)
                    .notAttendingRsvps(notAttendingRsvps)
                    .lastUpdated(LocalDateTime.now().format(DATE_FORMATTER))
                    .attendingFamilyMembers(attendingFamilyMembersData)
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
                .dietaryRestrictions(rsvp.getDietaryRestrictions())
                .submittedAt(rsvp.getSubmittedAt())
                .build();
    }

    /**
     * Create a temporary family group for a guest with plus-one
     * @param guest The primary guest
     * @return The created family group
     */
    private FamilyGroupEntity createTemporaryFamilyGroupForPlusOne(GuestEntity guest) {
        log.info("Creating temporary family group for plus-one guest: {} {}", 
                guest.getFirstName(), guest.getLastName());
        
        FamilyGroupEntity familyGroup = FamilyGroupEntity.builder()
                .groupName(guest.getFirstName() + " " + guest.getLastName() + " + Guest")
                .maxAttendees(2) // Primary guest + plus-one
                .createdAt(OffsetDateTime.now())
                .primaryContact(guest)
                .build();
        
        // Save the family group
        FamilyGroupEntity savedFamilyGroup = familyGroupRepository.save(familyGroup);
        
        // Update the guest to be in this family group
        guest.setFamilyGroup(savedFamilyGroup);
        guest.setIsPrimaryContact(true);
        guestDao.updateGuest(guest);
        
        log.info("Created temporary family group ID: {} for plus-one", savedFamilyGroup.getId());
        return savedFamilyGroup;
    }

}
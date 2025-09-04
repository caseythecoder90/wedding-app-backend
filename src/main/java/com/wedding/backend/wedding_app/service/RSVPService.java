package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.config.EmailConfig;
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
import com.wedding.backend.wedding_app.repository.FamilyMemberRepository;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final GuestRepository guestRepository;
    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final EmailService emailService;
    private final EmailConfig emailConfig;

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

        // Validate the guest exists
        GuestEntity guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> WeddingAppException.guestNotFound(request.getGuestId()));

        // Save the primary guest RSVP
        RSVPEntity savedRSVP = rsvpDao.saveRSVP(
                request.getGuestId(),
                request.getAttending(),
                request.getDietaryRestrictions());

        // Process family member RSVPs - this handles both family groups and plus-ones as family members
        if (request.getFamilyMembers() != null && !request.getFamilyMembers().isEmpty()) {
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
            
            processFamilyMemberRSVPs(request.getFamilyMembers(), familyGroup);
        }

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
            emailService.sendGuestConfirmationEmailAsync(savedRSVP, guest);
        }

        // 2. Always send admin notification (if enabled) - independent of guest confirmation
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
            resetFamilyMembersAttendance(guest.getFamilyGroup());
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
            resetFamilyMembersAttendance(guest.getFamilyGroup());
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
            List<FamilyMemberEntity> attendingFamilyMembers = familyMemberRepository.findAllAttending();
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
     * Process family member RSVPs for a family group with maxAttendees validation
     * @param familyMemberRequests List of family member RSVP requests
     * @param familyGroup The family group entity
     */
    private void processFamilyMemberRSVPs(List<RSVPRequestDTO.FamilyMemberRSVPRequest> familyMemberRequests, 
                                         FamilyGroupEntity familyGroup) {
        log.info("STARTED - Processing {} family member RSVPs for group: {}", 
                familyMemberRequests.size(), familyGroup.getGroupName());

        // Validate total doesn't exceed maxAttendees (primary guest + family members)
        int totalRequestedAttendees = 1; // primary guest
        totalRequestedAttendees += (int) familyMemberRequests.stream()
                .filter(req -> Boolean.TRUE.equals(req.getIsAttending()))
                .count();
                
        if (familyGroup.getMaxAttendees() != null && totalRequestedAttendees > familyGroup.getMaxAttendees()) {
            log.warn("Family group {} requested {} attendees but max is {}", 
                    familyGroup.getGroupName(), totalRequestedAttendees, familyGroup.getMaxAttendees());
            throw WeddingAppException.invalidParameter("familyMembers - exceeds maxAttendees limit");
        }

        for (RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest : familyMemberRequests) {
            try {
                FamilyMemberEntity familyMember;
                
                if (memberRequest.getFamilyMemberId() != null) {
                    // Update existing family member
                    familyMember = familyMemberRepository.findById(memberRequest.getFamilyMemberId())
                            .orElseThrow(() -> WeddingAppException.familyMemberNotFound(memberRequest.getFamilyMemberId()));
                    
                    log.info("Updating existing family member: {} {}", 
                            memberRequest.getFirstName(), memberRequest.getLastName());
                    
                    // Update fields
                    familyMember.setFirstName(memberRequest.getFirstName());
                    familyMember.setLastName(memberRequest.getLastName());
                    familyMember.setAgeGroup(normalizeAgeGroup(memberRequest.getAgeGroup()));
                    familyMember.setIsAttending(memberRequest.getIsAttending());
                    familyMember.setDietaryRestrictions(memberRequest.getDietaryRestrictions());
                    
                } else {
                    // Check if family member already exists by name (case-insensitive) to prevent duplicates
                    String requestFirstName = memberRequest.getFirstName().trim().toLowerCase();
                    String requestLastName = memberRequest.getLastName().trim().toLowerCase();
                    
                    Optional<FamilyMemberEntity> existingMember = familyGroup.getFamilyMembers().stream()
                            .filter(member -> 
                                member.getFirstName().trim().toLowerCase().equals(requestFirstName) &&
                                member.getLastName().trim().toLowerCase().equals(requestLastName))
                            .findFirst();
                    
                    if (existingMember.isPresent()) {
                        // Update existing family member found by name
                        familyMember = existingMember.get();
                        log.info("Found existing family member by name (case-insensitive), updating: {} {} (ID: {})", 
                                memberRequest.getFirstName(), memberRequest.getLastName(), familyMember.getId());
                        
                        familyMember.setAgeGroup(normalizeAgeGroup(memberRequest.getAgeGroup()));
                        familyMember.setIsAttending(memberRequest.getIsAttending());
                        familyMember.setDietaryRestrictions(memberRequest.getDietaryRestrictions());
                    } else {
                        // Create new family member (additional guest)
                        log.info("Creating new family member/additional guest: {} {}", 
                                memberRequest.getFirstName(), memberRequest.getLastName());
                        
                        familyMember = FamilyMemberEntity.builder()
                                .firstName(memberRequest.getFirstName())
                                .lastName(memberRequest.getLastName())
                                .ageGroup(normalizeAgeGroup(memberRequest.getAgeGroup()))
                                .isAttending(memberRequest.getIsAttending())
                                .dietaryRestrictions(memberRequest.getDietaryRestrictions())
                                .familyGroup(familyGroup)
                                .build();
                    }
                }
                
                // Save the family member
                familyMemberRepository.save(familyMember);
                
            } catch (Exception e) {
                log.error("Error processing family member RSVP: {} {}", 
                        memberRequest.getFirstName(), memberRequest.getLastName(), e);
                // Continue processing other family members even if one fails
            }
        }
        
        log.info("COMPLETED - Processed family member RSVPs for group: {}", familyGroup.getGroupName());
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
     * Reset all family members' attendance status to null when RSVP is deleted
     * @param familyGroup The family group whose members need to be reset
     */
    private void resetFamilyMembersAttendance(FamilyGroupEntity familyGroup) {
        log.info("STARTED - Resetting family members' attendance for group: {}", familyGroup.getGroupName());
        
        List<FamilyMemberEntity> familyMembers = familyGroup.getFamilyMembers();
        if (familyMembers != null && !familyMembers.isEmpty()) {
            for (FamilyMemberEntity member : familyMembers) {
                log.info("Resetting attendance for family member: {} {} (ID: {})", 
                        member.getFirstName(), member.getLastName(), member.getId());
                member.setIsAttending(null);
                member.setDietaryRestrictions(null);
            }
            
            // Save all updated family members
            familyMemberRepository.saveAll(familyMembers);
            log.info("Reset attendance status for {} family members", familyMembers.size());
        }
        
        log.info("COMPLETED - Reset family members' attendance for group: {}", familyGroup.getGroupName());
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
        guestRepository.save(guest);
        
        log.info("Created temporary family group ID: {} for plus-one", savedFamilyGroup.getId());
        return savedFamilyGroup;
    }

    /**
     * Normalize age group to consistent lowercase format
     * @param ageGroup The age group string to normalize
     * @return Normalized age group string in lowercase
     */
    private String normalizeAgeGroup(String ageGroup) {
        if (StringUtils.isBlank(ageGroup)) {
            return ageGroup;
        }
        
        return ageGroup.trim().toLowerCase();
    }
}
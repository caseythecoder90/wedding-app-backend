package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.dao.InvitationDao;
import com.wedding.backend.wedding_app.dao.RSVPDao;
import com.wedding.backend.wedding_app.dto.FamilyGroupResponseDTO;
import com.wedding.backend.wedding_app.dto.FamilyMemberResponseDTO;
import com.wedding.backend.wedding_app.dto.GuestResponseDTO;
import com.wedding.backend.wedding_app.dto.InvitationValidationResponseDTO;
import com.wedding.backend.wedding_app.dto.RSVPResponseDTO;
import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.entity.InvitationCodeEntity;
import com.wedding.backend.wedding_app.entity.RSVPEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CHARSET;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CODE_LENGTH;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CODE_PREFIX;
import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.SPACE;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvitationCodeService {

    private final InvitationDao invitationDao;
    private final GuestDao guestDao;
    private final RSVPDao rsvpDao;

    /**
     * Generates a unique random alphanumeric code
     * Format: WED-XXXXXX where X is an alphanumeric character
     */
    public String generateUniqueCode() {
        log.info("BEGIN - Generating unique invitation code");

        StringBuilder sb = new StringBuilder(CODE_PREFIX);
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

        GuestEntity guest = guestDao.findGuestById(guestId)
                .orElseThrow(() -> {
                    log.error("Guest not found with ID: {}", guestId);
                    return WeddingAppException.guestNotFound(guestId);
                });

        try {
            String code = generateUniqueCode();

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

        InvitationCodeEntity invitationCode = invitationDao.findInvitationByCode(code)
                .orElseThrow(() -> {
                    log.warn("Invalid invitation code - throwing exception");
                    return WeddingAppException.invalidInvitationCode(code);
                });

        if (invitationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Expired invitation code: {}", code);
            throw WeddingAppException.expiredInvitationCode(code);
        }

        log.info("END - Validated invitation code for guest: {}", invitationCode.getGuest().getId());
        return invitationCode.getGuest();
    }

    /**
     * Validates an invitation code and returns a DTO with guest and RSVP information (if exists)
     * @param code The invitation code to validate
     * @return InvitationValidationResponseDTO containing guest and RSVP information
     * @throws WeddingAppException if code is invalid or expired
     */
    public InvitationValidationResponseDTO validateInvitationAndRetrieveRSVP(String code) {
        log.info("BEGIN - Validating invitation code and retrieving RSVP data: {}", code);

        GuestEntity primaryGuest = validateCode(code);

        GuestResponseDTO primaryGuestDTO = buildGuestResponseDTO(primaryGuest);

        RSVPResponseDTO existingRsvpDTO = null;
        boolean hasExistingRsvp = Objects.nonNull(primaryGuest.getRsvp());
        
        if (BooleanUtils.isTrue(hasExistingRsvp)) {
            log.info("Guest has an existing RSVP, retrieving RSVP data");
            existingRsvpDTO = buildRSVPResponseDTO(primaryGuest.getRsvp(), primaryGuest);
        }

        InvitationValidationResponseDTO.GuestType guestType;
        FamilyGroupResponseDTO familyGroupDTO = null;
        List<FamilyMemberResponseDTO> familyMembersDTO = new ArrayList<>();
        FamilyGroupEntity familyGroup = primaryGuest.getFamilyGroup();
        
        if (Objects.nonNull(familyGroup) && BooleanUtils.isTrue(primaryGuest.getIsPrimaryContact())) {

            guestType = InvitationValidationResponseDTO.GuestType.FAMILY_PRIMARY;
            
            log.info("Guest is primary contact for group: {}", familyGroup.getGroupName());

            familyGroupDTO = FamilyGroupResponseDTO.builder()
                    .id(familyGroup.getId())
                    .groupName(familyGroup.getGroupName())
                    .maxAttendees(familyGroup.getMaxAttendees())
                    .primaryContactGuestId(primaryGuest.getId())
                    .createdAt(familyGroup.getCreatedAt())
                    .build();

            if (CollectionUtils.isNotEmpty(familyGroup.getFamilyMembers())) {
                familyMembersDTO = selectFamilyMembersForInvitation(familyGroup);
            }
            
        } else if (BooleanUtils.isTrue(primaryGuest.getPlusOneAllowed())) {
            guestType = InvitationValidationResponseDTO.GuestType.SOLO_WITH_PLUS_ONE;
        } else {
            guestType = InvitationValidationResponseDTO.GuestType.SOLO;
        }

        InvitationValidationResponseDTO response = InvitationValidationResponseDTO.builder()
                .primaryGuest(primaryGuestDTO)
                .existingRsvp(existingRsvpDTO)
                .hasExistingRsvp(hasExistingRsvp)
                .guestType(guestType)
                .familyGroup(familyGroupDTO)
                .familyMembers(familyMembersDTO)
                .canBringPlusOne(primaryGuest.getPlusOneAllowed())
                .build();

        log.info("END - Validated invitation code for {} guest: {}", guestType, primaryGuest.getId());
        return response;
    }

    private GuestResponseDTO buildGuestResponseDTO(GuestEntity guest) {
        return GuestResponseDTO.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .plusOneAllowed(guest.getPlusOneAllowed())
                .hasRsvp(Objects.nonNull(guest.getRsvp()))
                .rsvpId(Objects.nonNull(guest.getRsvp()) ? guest.getRsvp().getId() : null)
                .build();
    }

    private RSVPResponseDTO buildRSVPResponseDTO(RSVPEntity rsvp, GuestEntity guest) {
        return RSVPResponseDTO.builder()
                .id(rsvp.getId())
                .guestId(guest.getId())
                .guestName(StringUtils.joinWith(SPACE, guest.getFirstName(), guest.getLastName()))
                .guestEmail(guest.getEmail())
                .attending(rsvp.getAttending())
                .dietaryRestrictions(rsvp.getDietaryRestrictions())
                .submittedAt(rsvp.getSubmittedAt())
                .build();
    }

    private FamilyMemberResponseDTO buildFamilyMemberResponseDTO(FamilyMemberEntity familyMember) {
        return FamilyMemberResponseDTO.builder()
                .id(familyMember.getId())
                .firstName(familyMember.getFirstName())
                .lastName(familyMember.getLastName())
                .ageGroup(familyMember.getAgeGroup())
                .dietaryRestrictions(familyMember.getDietaryRestrictions())
                .isAttending(familyMember.getIsAttending())
                .familyGroupId(familyMember.getFamilyGroup().getId())
                .build();
    }

    /**
     * Selects family members to return for invitation validation, respecting maxAttendees limit.
     * Prioritizes attending members, then non-attending members up to the limit.
     * @param familyGroup The family group entity
     * @return List of family member DTOs limited by maxAttendees
     */
    private List<FamilyMemberResponseDTO> selectFamilyMembersForInvitation(FamilyGroupEntity familyGroup) {
        log.info("Selecting family members for invitation validation, group: {}, maxAttendees: {}", 
                familyGroup.getGroupName(), familyGroup.getMaxAttendees());

        List<FamilyMemberEntity> allFamilyMembers = familyGroup.getFamilyMembers();

        if (Objects.isNull(familyGroup.getMaxAttendees())) {
            log.info("No maxAttendees limit, returning all {} family members", allFamilyMembers.size());
            return allFamilyMembers.stream()
                    .map(this::buildFamilyMemberResponseDTO)
                    .collect(Collectors.toList());
        }

        // Calculate how many family members we can include (maxAttendees - 1 for primary guest)
        int maxFamilyMembers = familyGroup.getMaxAttendees() - 1;
        
        if (maxFamilyMembers <= 0) {
            log.info("MaxAttendees is 1 or less, no family members can be included");
            return new ArrayList<>();
        }

        // Separate attending and non-attending members
        List<FamilyMemberEntity> attendingMembers = allFamilyMembers.stream()
                .filter(member -> BooleanUtils.isTrue(member.getIsAttending()))
                .toList();
                
        List<FamilyMemberEntity> nonAttendingMembers = allFamilyMembers.stream()
                .filter(member -> BooleanUtils.isNotTrue(member.getIsAttending()))
                .toList();

        // First, add all attending members (they should always be shown)
        List<FamilyMemberEntity> selectedMembers = new ArrayList<>(attendingMembers);
        
        // Then add non-attending members up to the limit
        int remainingSlots = maxFamilyMembers - attendingMembers.size();
        if (remainingSlots > 0) {
            selectedMembers.addAll(nonAttendingMembers.stream()
                    .limit(remainingSlots)
                    .toList());
        }

        log.info("Selected {} family members (attending: {}, non-attending: {}) from total {} members", 
                selectedMembers.size(), attendingMembers.size(), 
                Math.min(remainingSlots, nonAttendingMembers.size()), allFamilyMembers.size());

        return selectedMembers.stream()
                .map(this::buildFamilyMemberResponseDTO)
                .collect(Collectors.toList());
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

        InvitationCodeEntity invCode = invitationDao.findInvitationByCode(code)
                .orElseThrow(() -> {
                    log.warn("Cannot mark as used - code not found: {}", code);
                    return WeddingAppException.invalidInvitationCode(code);
                });

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
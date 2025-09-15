package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.FamilyMemberDao;
import com.wedding.backend.wedding_app.dto.RSVPRequestDTO;
import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberDao familyMemberDao;

    /**
     * Process family member RSVPs for a family group with maxAttendees validation
     * @param familyMemberRequests List of family member RSVP requests
     * @param familyGroup The family group entity
     */
    @Transactional
    public void processFamilyMemberRSVPs(
            List<RSVPRequestDTO.FamilyMemberRSVPRequest> familyMemberRequests,
            FamilyGroupEntity familyGroup) {

        log.info("STARTED - Processing {} family member RSVPs for group: {}",
                familyMemberRequests.size(), familyGroup.getGroupName());

        validateMaxAttendeesLimit(familyMemberRequests, familyGroup);
        resetAllFamilyMembersAttendance(familyGroup);
        processIndividualFamilyMembers(familyMemberRequests, familyGroup);
        
        log.info("COMPLETED - Processed family member RSVPs for group: {}", familyGroup.getGroupName());
    }

    /**
     * Reset all family members' attendance status for a family group
     * @param familyGroup The family group whose members need to be reset
     */
    @Transactional
    public void resetAllFamilyMembersAttendance(FamilyGroupEntity familyGroup) {
        log.info("STARTED - Resetting family members' attendance for group: {}", familyGroup.getGroupName());
        
        List<FamilyMemberEntity> familyMembers = familyGroup.getFamilyMembers();
        if (CollectionUtils.isNotEmpty(familyMembers)) {
            for (FamilyMemberEntity member : familyMembers) {
                log.info("Resetting attendance for family member: {} {} (ID: {})", 
                        member.getFirstName(), member.getLastName(), member.getId());
                member.setIsAttending(false);
                member.setDietaryRestrictions(null);
            }
            
            familyMemberDao.saveAll(familyMembers);
            log.info("Reset attendance status for {} family members", familyMembers.size());
        }
        
        log.info("COMPLETED - Reset family members' attendance for group: {}", familyGroup.getGroupName());
    }

    /**
     * Validate that requested attendees don't exceed maxAttendees limit
     */
    private void validateMaxAttendeesLimit(List<RSVPRequestDTO.FamilyMemberRSVPRequest> familyMemberRequests, 
                                          FamilyGroupEntity familyGroup) {
        int totalRequestedAttendees = 1;
        totalRequestedAttendees += (int) familyMemberRequests.stream()
                .filter(req -> BooleanUtils.isTrue(req.getIsAttending()))
                .count();
                
        if (familyGroup.getMaxAttendees() != null && totalRequestedAttendees > familyGroup.getMaxAttendees()) {
            log.warn("Family group {} requested {} attendees but max is {}", 
                    familyGroup.getGroupName(), totalRequestedAttendees, familyGroup.getMaxAttendees());
            throw WeddingAppException.invalidParameter("familyMembers - exceeds maxAttendees limit");
        }
    }

    /**
     * Process individual family member requests
     */
    private void processIndividualFamilyMembers(List<RSVPRequestDTO.FamilyMemberRSVPRequest> familyMemberRequests,
                                               FamilyGroupEntity familyGroup) {
        for (RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest : familyMemberRequests) {
            try {
                FamilyMemberEntity familyMember = findOrCreateFamilyMember(memberRequest, familyGroup);
                updateFamilyMemberDetails(familyMember, memberRequest);
                familyMemberDao.save(familyMember);
                
            } catch (Exception e) {
                log.error("Error processing family member RSVP: {} {}", 
                        memberRequest.getFirstName(), memberRequest.getLastName(), e);
                // Continue processing other family members even if one fails
            }
        }
    }

    /**
     * Find existing family member by ID or name, or create new one
     */
    private FamilyMemberEntity findOrCreateFamilyMember(RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest,
                                                       FamilyGroupEntity familyGroup) {
        if (memberRequest.getFamilyMemberId() != null) {
            return findExistingMemberById(memberRequest);
        } else {
            return findExistingMemberByName(memberRequest, familyGroup)
                    .orElseGet(() -> createNewFamilyMember(memberRequest, familyGroup));
        }
    }

    /**
     * Find existing family member by ID
     */
    private FamilyMemberEntity findExistingMemberById(RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest) {
        log.info("Updating existing family member: {} {}", 
                memberRequest.getFirstName(), memberRequest.getLastName());
        
        return familyMemberDao.findById(memberRequest.getFamilyMemberId())
                .orElseThrow(() -> WeddingAppException.familyMemberNotFound(memberRequest.getFamilyMemberId()));
    }

    /**
     * Find existing family member by name (case-insensitive)
     */
    private Optional<FamilyMemberEntity> findExistingMemberByName(RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest,
                                                                 FamilyGroupEntity familyGroup) {
        if (!hasValidName(memberRequest)) {
            return Optional.empty();
        }

        String requestFirstName = normalizeString(memberRequest.getFirstName());
        String requestLastName = normalizeString(memberRequest.getLastName());
        
        return familyGroup.getFamilyMembers().stream()
                .filter(member -> isNameMatch(member, requestFirstName, requestLastName))
                .findFirst()
                .map(member -> {
                    log.info("Found existing family member by name (case-insensitive), updating: {} {} (ID: {})", 
                            memberRequest.getFirstName(), memberRequest.getLastName(), member.getId());
                    return member;
                });
    }

    /**
     * Create new family member entity
     */
    private FamilyMemberEntity createNewFamilyMember(RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest,
                                                    FamilyGroupEntity familyGroup) {
        log.info("Creating new family member/additional guest: {} {}", 
                memberRequest.getFirstName(), memberRequest.getLastName());
        
        return FamilyMemberEntity.builder()
                .firstName(memberRequest.getFirstName())
                .lastName(memberRequest.getLastName())
                .ageGroup(normalizeAgeGroup(memberRequest.getAgeGroup()))
                .isAttending(memberRequest.getIsAttending())
                .dietaryRestrictions(memberRequest.getDietaryRestrictions())
                .familyGroup(familyGroup)
                .build();
    }

    /**
     * Update family member details from request
     */
    private void updateFamilyMemberDetails(FamilyMemberEntity familyMember, 
                                         RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest) {
        familyMember.setFirstName(memberRequest.getFirstName());
        familyMember.setLastName(memberRequest.getLastName());
        familyMember.setAgeGroup(normalizeAgeGroup(memberRequest.getAgeGroup()));
        familyMember.setIsAttending(memberRequest.getIsAttending());
        familyMember.setDietaryRestrictions(memberRequest.getDietaryRestrictions());
    }

    /**
     * Check if member name matches the request (case-insensitive)
     */
    private boolean isNameMatch(FamilyMemberEntity member, String requestFirstName, String requestLastName) {
        return normalizeString(member.getFirstName()).equals(requestFirstName) &&
               normalizeString(member.getLastName()).equals(requestLastName);
    }

    /**
     * Check if request has valid name fields
     */
    private boolean hasValidName(RSVPRequestDTO.FamilyMemberRSVPRequest memberRequest) {
        return StringUtils.isNotBlank(memberRequest.getFirstName()) && 
               StringUtils.isNotBlank(memberRequest.getLastName());
    }

    /**
     * Normalize string for comparison (trim, lowercase, handle nulls)
     */
    private String normalizeString(String input) {
        return StringUtils.isBlank(input) ? "" : input.trim().toLowerCase();
    }

    /**
     * Normalize age group to consistent lowercase format
     */
    private String normalizeAgeGroup(String ageGroup) {
        return StringUtils.isBlank(ageGroup) ? ageGroup : ageGroup.trim().toLowerCase();
    }
}
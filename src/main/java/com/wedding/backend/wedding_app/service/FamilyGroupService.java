package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.FamilyGroupDao;
import com.wedding.backend.wedding_app.dao.FamilyMemberDao;
import com.wedding.backend.wedding_app.dao.GuestDao;
import com.wedding.backend.wedding_app.dto.FamilyGroupResponseDTO;
import com.wedding.backend.wedding_app.dto.FamilyMemberResponseDTO;
import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.model.request.FamilyGroupRequest;
import com.wedding.backend.wedding_app.model.request.FamilyMemberRequest;
import com.wedding.backend.wedding_app.model.request.GuestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyGroupService {

    private final FamilyGroupDao familyGroupDao;
    private final GuestDao guestDao;
    private final FamilyMemberDao familyMemberDao;

    /**
     * Create a complete family group with primary contact, additional guests, and family members
     */
    @Transactional
    public FamilyGroupResponseDTO createFamilyGroup(FamilyGroupRequest request) {
        log.info("BEGIN - Creating family group: {}", request.getGroupName());

        try {
            // 1. Create primary contact guest
            GuestEntity primaryContact = createGuest(request.getPrimaryContact(), true);
            
            // 2. Create the family group
            FamilyGroupEntity familyGroup = FamilyGroupEntity.builder()
                    .groupName(request.getGroupName())
                    .maxAttendees(request.getMaxAttendees())
                    .createdAt(OffsetDateTime.now())
                    .primaryContact(primaryContact)
                    .build();

            FamilyGroupEntity savedFamilyGroup = familyGroupDao.save(familyGroup);

            // 3. Update primary contact with family group reference
            primaryContact.setFamilyGroup(savedFamilyGroup);
            primaryContact.setIsPrimaryContact(true);
            guestDao.updateGuest(primaryContact);

            // 4. Create family members if provided
            if (request.getFamilyMembers() != null && !request.getFamilyMembers().isEmpty()) {
                for (FamilyMemberRequest memberRequest : request.getFamilyMembers()) {
                    FamilyMemberEntity familyMember = createFamilyMember(memberRequest, savedFamilyGroup);
                    familyMemberDao.save(familyMember);
                }
            }

            log.info("END - Family group created successfully with ID: {}", savedFamilyGroup.getId());
            return mapToResponseDTO(savedFamilyGroup);

        } catch (Exception e) {
            log.error("Error creating family group: {}", request.getGroupName(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all family groups
     */
    @Transactional(readOnly = true)
    public List<FamilyGroupResponseDTO> getAllFamilyGroups() {
        log.info("BEGIN - Fetching all family groups");

        try {
            List<FamilyGroupEntity> familyGroups = familyGroupDao.findAllWithPrimaryContacts();
            List<FamilyGroupResponseDTO> responseDTOs = new ArrayList<>();

            for (FamilyGroupEntity familyGroup : familyGroups) {
                responseDTOs.add(mapToResponseDTO(familyGroup));
            }

            log.info("END - Found {} family groups", responseDTOs.size());
            return responseDTOs;
        } catch (Exception e) {
            log.error("Error fetching all family groups", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get family group by ID
     */
    @Transactional(readOnly = true)
    public FamilyGroupResponseDTO getFamilyGroupById(Long id) {
        log.info("BEGIN - Fetching family group with ID: {}", id);

        try {
            Optional<FamilyGroupEntity> familyGroupOpt = familyGroupDao.findById(id);
            if (familyGroupOpt.isEmpty()) {
                throw WeddingAppException.familyGroupNotFound(id);
            }

            FamilyGroupResponseDTO responseDTO = mapToResponseDTO(familyGroupOpt.get());
            log.info("END - Family group found: {}", responseDTO.getGroupName());
            return responseDTO;
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching family group with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Add a family member to existing family group
     */
    @Transactional
    public FamilyMemberResponseDTO addFamilyMember(Long familyGroupId, FamilyMemberRequest request) {
        log.info("BEGIN - Adding family member {} {} to family group: {}", 
                request.getFirstName(), request.getLastName(), familyGroupId);

        try {
            // Verify family group exists
            Optional<FamilyGroupEntity> familyGroupOpt = familyGroupDao.findById(familyGroupId);
            if (familyGroupOpt.isEmpty()) {
                throw WeddingAppException.familyGroupNotFound(familyGroupId);
            }

            FamilyGroupEntity familyGroup = familyGroupOpt.get();
            FamilyMemberEntity familyMember = createFamilyMember(request, familyGroup);
            FamilyMemberEntity savedFamilyMember = familyMemberDao.save(familyMember);

            log.info("END - Family member added successfully with ID: {}", savedFamilyMember.getId());
            return mapFamilyMemberToResponseDTO(savedFamilyMember);
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error adding family member to family group: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all family members for a family group
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberResponseDTO> getFamilyMembers(Long familyGroupId) {
        log.info("BEGIN - Fetching family members for family group: {}", familyGroupId);

        try {
            List<FamilyMemberEntity> familyMembers = familyMemberDao.findByFamilyGroupId(familyGroupId);
            List<FamilyMemberResponseDTO> responseDTOs = new ArrayList<>();

            for (FamilyMemberEntity familyMember : familyMembers) {
                responseDTOs.add(mapFamilyMemberToResponseDTO(familyMember));
            }

            log.info("END - Found {} family members for family group: {}", responseDTOs.size(), familyGroupId);
            return responseDTOs;
        } catch (Exception e) {
            log.error("Error fetching family members for family group: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete a family group and all associated data
     */
    @Transactional
    public void deleteFamilyGroup(Long id) {
        log.info("BEGIN - Deleting family group with ID: {}", id);

        try {
            Optional<FamilyGroupEntity> familyGroupOpt = familyGroupDao.findById(id);
            if (familyGroupOpt.isEmpty()) {
                throw WeddingAppException.familyGroupNotFound(id);
            }

            familyGroupDao.deleteById(id);
            log.info("END - Family group deleted successfully: {}", id);
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting family group with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    private GuestEntity createGuest(GuestRequest guestRequest, boolean isPrimaryContact) {
        return guestDao.saveGuest(
                guestRequest.getFirstName(),
                guestRequest.getLastName(),
                guestRequest.getEmail(),
                guestRequest.getPhone(),
                guestRequest.isPlusOneAllowed()
        );
    }

    private FamilyMemberEntity createFamilyMember(FamilyMemberRequest request, FamilyGroupEntity familyGroup) {
        return FamilyMemberEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .ageGroup(request.getAgeGroup())
                .dietaryRestrictions(request.getDietaryRestrictions())
                .isAttending(request.getIsAttending())
                .familyGroup(familyGroup)
                .build();
    }

    private FamilyGroupResponseDTO mapToResponseDTO(FamilyGroupEntity familyGroup) {
        return FamilyGroupResponseDTO.builder()
                .id(familyGroup.getId())
                .groupName(familyGroup.getGroupName())
                .maxAttendees(familyGroup.getMaxAttendees())
                .primaryContactGuestId(familyGroup.getPrimaryContact() != null 
                        ? familyGroup.getPrimaryContact().getId() : null)
                .createdAt(familyGroup.getCreatedAt())
                .build();
    }

    private FamilyMemberResponseDTO mapFamilyMemberToResponseDTO(FamilyMemberEntity familyMember) {
        return FamilyMemberResponseDTO.builder()
                .id(familyMember.getId())
                .firstName(familyMember.getFirstName())
                .lastName(familyMember.getLastName())
                .ageGroup(familyMember.getAgeGroup())
                .dietaryRestrictions(familyMember.getDietaryRestrictions())
                .isAttending(familyMember.getIsAttending())
                .familyGroupId(familyMember.getFamilyGroup() != null 
                        ? familyMember.getFamilyGroup().getId() : null)
                .build();
    }
}
package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.FamilyMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class FamilyMemberDao {

    private final FamilyMemberRepository familyMemberRepository;
    private final Logger log = LoggerFactory.getLogger(FamilyMemberDao.class);

    public FamilyMemberDao(FamilyMemberRepository familyMemberRepository) {
        this.familyMemberRepository = familyMemberRepository;
    }

    /**
     * Find family member by ID
     */
    @Transactional(readOnly = true)
    public Optional<FamilyMemberEntity> findById(Long id) {
        try {
            return familyMemberRepository.findById(id);
        } catch (Exception e) {
            log.error("Error finding family member with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find all family members for a family group
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findByFamilyGroupId(Long familyGroupId) {
        log.info("Finding family members for family group ID: {}", familyGroupId);

        try {
            List<FamilyMemberEntity> familyMembers = familyMemberRepository.findByFamilyGroupId(familyGroupId);
            log.info("Found {} family members for family group: {}", familyMembers.size(), familyGroupId);
            return familyMembers;
        } catch (Exception e) {
            log.error("Error finding family members for family group ID: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find attending family members for a family group
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findAttendingByFamilyGroupId(Long familyGroupId) {
        log.info("Finding attending family members for family group ID: {}", familyGroupId);

        try {
            List<FamilyMemberEntity> attendingMembers = 
                familyMemberRepository.findByFamilyGroupIdAndIsAttending(familyGroupId, true);
            log.info("Found {} attending family members for family group: {}", attendingMembers.size(), familyGroupId);
            return attendingMembers;
        } catch (Exception e) {
            log.error("Error finding attending family members for family group ID: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all family members
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findAll() {
        log.info("Fetching all family members");

        try {
            List<FamilyMemberEntity> familyMembers = familyMemberRepository.findAll();
            log.info("Found {} family members", familyMembers.size());
            return familyMembers;
        } catch (Exception e) {
            log.error("Error fetching all family members", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Create a new family member
     */
    @Transactional
    public FamilyMemberEntity save(Long familyGroupId, String firstName, String lastName, 
                                  String ageGroup, String dietaryRestrictions, Boolean isAttending) {
        log.info("Creating new family member: {} {} for family group: {}", firstName, lastName, familyGroupId);

        try {
            FamilyMemberEntity familyMember = FamilyMemberEntity.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .ageGroup(ageGroup)
                    .dietaryRestrictions(dietaryRestrictions)
                    .isAttending(isAttending)
                    .build();

            FamilyMemberEntity savedFamilyMember = familyMemberRepository.save(familyMember);
            log.info("Family member created and saved: {}", savedFamilyMember);
            return savedFamilyMember;
        } catch (Exception e) {
            log.error("Error saving family member to DB", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Save a family member entity
     */
    @Transactional
    public FamilyMemberEntity save(FamilyMemberEntity familyMember) {
        log.info("Saving family member: {} {}", familyMember.getFirstName(), familyMember.getLastName());

        try {
            FamilyMemberEntity savedFamilyMember = familyMemberRepository.save(familyMember);
            log.info("Family member saved successfully: {}", savedFamilyMember);
            return savedFamilyMember;
        } catch (Exception e) {
            log.error("Error saving family member to DB", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Save multiple family members
     */
    @Transactional
    public List<FamilyMemberEntity> saveAll(List<FamilyMemberEntity> familyMembers) {
        log.info("Saving {} family members", familyMembers.size());

        try {
            List<FamilyMemberEntity> savedFamilyMembers = familyMemberRepository.saveAll(familyMembers);
            log.info("Successfully saved {} family members", savedFamilyMembers.size());
            return savedFamilyMembers;
        } catch (Exception e) {
            log.error("Error saving family members to DB", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Update an existing family member
     */
    @Transactional
    public FamilyMemberEntity update(FamilyMemberEntity familyMember) {
        log.info("Updating family member with ID: {}", familyMember.getId());

        try {
            if (familyMember.getId() == null || !familyMemberRepository.existsById(familyMember.getId())) {
                throw WeddingAppException.familyMemberNotFound(familyMember.getId());
            }

            FamilyMemberEntity updatedFamilyMember = familyMemberRepository.save(familyMember);
            log.info("Family member updated successfully: {}", updatedFamilyMember);
            return updatedFamilyMember;
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating family member with ID: {}", familyMember.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete a family member by ID
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting family member with ID: {}", id);

        try {
            if (!familyMemberRepository.existsById(id)) {
                log.warn("No family member found with ID: {} to delete", id);
                throw WeddingAppException.familyMemberNotFound(id);
            }

            familyMemberRepository.deleteById(id);
            log.info("Family member with ID: {} deleted successfully", id);
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting family member with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete all family members for a family group
     */
    @Transactional
    public void deleteByFamilyGroupId(Long familyGroupId) {
        log.info("Deleting all family members for family group ID: {}", familyGroupId);

        try {
            familyMemberRepository.deleteByFamilyGroupId(familyGroupId);
            log.info("All family members deleted for family group ID: {}", familyGroupId);
        } catch (Exception e) {
            log.error("Error deleting family members for family group ID: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Count family members in a family group
     */
    @Transactional(readOnly = true)
    public long countByFamilyGroupId(Long familyGroupId) {
        try {
            return familyMemberRepository.countByFamilyGroupId(familyGroupId);
        } catch (Exception e) {
            log.error("Error counting family members for family group ID: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Count attending family members in a family group
     */
    @Transactional(readOnly = true)
    public long countAttendingByFamilyGroupId(Long familyGroupId) {
        try {
            return familyMemberRepository.countAttendingByFamilyGroupId(familyGroupId);
        } catch (Exception e) {
            log.error("Error counting attending family members for family group ID: {}", familyGroupId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find family members by age group
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findByAgeGroup(String ageGroup) {
        log.info("Finding family members by age group: {}", ageGroup);

        try {
            List<FamilyMemberEntity> familyMembers = familyMemberRepository.findByAgeGroup(ageGroup);
            log.info("Found {} family members with age group: {}", familyMembers.size(), ageGroup);
            return familyMembers;
        } catch (Exception e) {
            log.error("Error finding family members by age group: {}", ageGroup, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find all family members who are attending
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findAllAttending() {
        log.info("Finding all attending family members");

        try {
            List<FamilyMemberEntity> attendingMembers = familyMemberRepository.findAllAttending();
            log.info("Found {} attending family members", attendingMembers.size());
            return attendingMembers;
        } catch (Exception e) {
            log.error("Error finding all attending family members", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find all family members with dietary restrictions
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberEntity> findAllWithDietaryRestrictions() {
        log.info("Finding all family members with dietary restrictions");

        try {
            List<FamilyMemberEntity> familyMembers = familyMemberRepository.findAllWithDietaryRestrictions();
            log.info("Found {} family members with dietary restrictions", familyMembers.size());
            return familyMembers;
        } catch (Exception e) {
            log.error("Error finding family members with dietary restrictions", e);
            throw WeddingAppException.databaseError();
        }
    }
}
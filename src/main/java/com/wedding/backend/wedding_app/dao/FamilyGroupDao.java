package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.FamilyGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FamilyGroupDao {

    private final FamilyGroupRepository familyGroupRepository;
    private final Logger log = LoggerFactory.getLogger(FamilyGroupDao.class);

    public FamilyGroupDao(FamilyGroupRepository familyGroupRepository) {
        this.familyGroupRepository = familyGroupRepository;
    }

    /**
     * Find family group by ID
     */
    @Transactional(readOnly = true)
    public Optional<FamilyGroupEntity> findById(Long id) {
        try {
            return familyGroupRepository.findById(id);
        } catch (Exception e) {
            log.error("Error finding family group with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find family group by primary contact guest ID
     */
    @Transactional(readOnly = true)
    public Optional<FamilyGroupEntity> findByPrimaryContactId(Long primaryContactGuestId) {
        log.info("Finding family group for primary contact guest ID: {}", primaryContactGuestId);
        
        try {
            Optional<FamilyGroupEntity> familyGroup = familyGroupRepository.findByPrimaryContactId(primaryContactGuestId);
            
            if (familyGroup.isPresent()) {
                log.info("Family group found for primary contact: {}", primaryContactGuestId);
            } else {
                log.debug("No family group found for primary contact: {}", primaryContactGuestId);
            }
            
            return familyGroup;
        } catch (Exception e) {
            log.error("Error finding family group by primary contact ID: {}", primaryContactGuestId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all family groups
     */
    @Transactional(readOnly = true)
    public List<FamilyGroupEntity> findAll() {
        log.info("Fetching all family groups");

        try {
            List<FamilyGroupEntity> familyGroups = familyGroupRepository.findAll();
            log.info("Found {} family groups", familyGroups.size());
            return familyGroups;
        } catch (Exception e) {
            log.error("Error fetching all family groups", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all family groups with their primary contacts loaded
     */
    @Transactional(readOnly = true)
    public List<FamilyGroupEntity> findAllWithPrimaryContacts() {
        log.info("Fetching all family groups with primary contacts");

        try {
            List<FamilyGroupEntity> familyGroups = familyGroupRepository.findAllWithPrimaryContacts();
            log.info("Found {} family groups with primary contacts", familyGroups.size());
            return familyGroups;
        } catch (Exception e) {
            log.error("Error fetching family groups with primary contacts", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Create a new family group
     */
    @Transactional
    public FamilyGroupEntity save(String groupName, Integer maxAttendees, Long primaryContactGuestId) {
        log.info("Creating new family group: {} with max attendees: {} for guest: {}", 
                groupName, maxAttendees, primaryContactGuestId);

        try {
            // Check if primary contact is already assigned to another family group
            if (familyGroupRepository.existsByPrimaryContactId(primaryContactGuestId)) {
                log.warn("Guest {} is already a primary contact for another family group", primaryContactGuestId);
                throw WeddingAppException.guestAlreadyPrimaryContact(primaryContactGuestId);
            }

            FamilyGroupEntity familyGroup = FamilyGroupEntity.builder()
                    .groupName(groupName)
                    .maxAttendees(maxAttendees)
                    .createdAt(OffsetDateTime.now())
                    .build();

            FamilyGroupEntity savedFamilyGroup = familyGroupRepository.save(familyGroup);
            log.info("Family group created and saved: {}", savedFamilyGroup);
            return savedFamilyGroup;
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving family group to DB", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Update an existing family group
     */
    @Transactional
    public FamilyGroupEntity update(FamilyGroupEntity familyGroup) {
        log.info("Updating family group with ID: {}", familyGroup.getId());

        try {
            if (familyGroup.getId() == null || !familyGroupRepository.existsById(familyGroup.getId())) {
                throw WeddingAppException.familyGroupNotFound(familyGroup.getId());
            }

            FamilyGroupEntity updatedFamilyGroup = familyGroupRepository.save(familyGroup);
            log.info("Family group updated successfully: {}", updatedFamilyGroup);
            return updatedFamilyGroup;
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating family group with ID: {}", familyGroup.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete a family group by ID
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting family group with ID: {}", id);

        try {
            if (!familyGroupRepository.existsById(id)) {
                log.warn("No family group found with ID: {} to delete", id);
                throw WeddingAppException.familyGroupNotFound(id);
            }

            familyGroupRepository.deleteById(id);
            log.info("Family group with ID: {} deleted successfully", id);
        } catch (WeddingAppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting family group with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Check if a guest is already a primary contact
     */
    @Transactional(readOnly = true)
    public boolean isGuestAlreadyPrimaryContact(Long guestId) {
        try {
            return familyGroupRepository.existsByPrimaryContactId(guestId);
        } catch (Exception e) {
            log.error("Error checking if guest {} is already a primary contact", guestId, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find family groups with multiple attendees allowed
     */
    @Transactional(readOnly = true)
    public List<FamilyGroupEntity> findFamilyGroupsWithMultipleAttendees() {
        log.info("Finding family groups with multiple attendees allowed");

        try {
            List<FamilyGroupEntity> familyGroups = familyGroupRepository.findFamilyGroupsWithMultipleAttendees();
            log.info("Found {} family groups with multiple attendees", familyGroups.size());
            return familyGroups;
        } catch (Exception e) {
            log.error("Error finding family groups with multiple attendees", e);
            throw WeddingAppException.databaseError();
        }
    }
}
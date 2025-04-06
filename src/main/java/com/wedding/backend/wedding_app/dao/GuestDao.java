package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.exception.DatabaseException;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GuestDao {

    private final GuestRepository guestRepository;
    
    /**
     * Find guest by ID
     * @param id The guest ID
     * @return Optional guest entity
     */
    @Transactional(readOnly = true)
    public Optional<GuestEntity> findGuestById(Long id) {
        try {
            return guestRepository.findById(id);
        } catch (Exception e) {
            log.error("Error finding guest with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find guest by first and last name (case insensitive)
     * @param firstName The guest's first name
     * @param lastName The guest's last name
     * @return Optional guest entity
     */
    @Transactional(readOnly = true)
    public Optional<GuestEntity> findGuestByFullName(String firstName, String lastName) {
        log.info("Fetching guest with firstName={}, lastName={}", firstName, lastName);

        try {
            Optional<GuestEntity> guestOpt = 
                    guestRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);

            if (guestOpt.isPresent()) {
                log.info("Guest found with name: {} {}", firstName, lastName);
            } else {
                log.warn("Guest with name: {} {} does not exist", firstName, lastName);
            }

            return guestOpt;
        } catch (Exception e) {
            log.error("Error fetching guest by name: {} {}", firstName, lastName, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all guests
     * @return List of all guest entities
     */
    @Transactional(readOnly = true)
    public List<GuestEntity> findAllGuests() {
        log.info("Fetching all guests");

        try {
            List<GuestEntity> guests = guestRepository.findAll();
            log.info("Found {} guests", guests.size());
            return guests;
        } catch (Exception e) {
            log.error("Error fetching all guests", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find guests by last name
     * @param lastName The last name to search for
     * @return List of matching guest entities
     */
    @Transactional(readOnly = true)
    public List<GuestEntity> findGuestsByLastName(String lastName) {
        log.info("Fetching guests with lastName={}", lastName);

        try {
            List<GuestEntity> guests = guestRepository.findByLastName(lastName);
            log.info("Found {} guests with last name: {}", guests.size(), lastName);
            return guests;
        } catch (Exception e) {
            log.error("Error fetching guests by last name: {}", lastName, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Create a new guest
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @param email Guest's email
     * @param phone Guest's phone number
     * @param plusOneAllowed Whether guest can bring a plus one
     * @return Created guest entity
     */
    @Transactional
    public GuestEntity saveGuest(
            String firstName,
            String lastName,
            String email,
            String phone,
            boolean plusOneAllowed) {

        log.info("Creating new guest: {} {}", firstName, lastName);

        try {
            // Check if guest already exists
            Optional<GuestEntity> existingGuest = 
                    guestRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
            
            if (existingGuest.isPresent()) {
                log.warn("Guest with name {} {} already exists", firstName, lastName);
                throw WeddingAppException.duplicateGuest(firstName, lastName);
            }

            GuestEntity guest = GuestEntity.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .plusOneAllowed(plusOneAllowed)
                    .build();

            GuestEntity savedGuest = guestRepository.save(guest);
            log.info("Guest created and saved to DB: {}", savedGuest);
            return savedGuest;
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error saving guest to DB", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Update an existing guest
     * @param guest The guest entity to update
     * @return Updated guest entity
     */
    @Transactional
    public GuestEntity updateGuest(GuestEntity guest) {
        log.info("Updating guest with ID: {}", guest.getId());

        try {
            // Ensure the guest exists
            if (guest.getId() == null || !guestRepository.existsById(guest.getId())) {
                throw WeddingAppException.guestNotFound(guest.getId());
            }

            GuestEntity updatedGuest = guestRepository.save(guest);
            log.info("Guest updated successfully: {}", updatedGuest);
            return updatedGuest;
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error updating guest with ID: {}", guest.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Delete a guest by ID
     * @param id The guest ID to delete
     */
    @Transactional
    public void deleteGuest(Long id) {
        log.info("Deleting guest with ID: {}", id);

        try {
            if (!guestRepository.existsById(id)) {
                log.warn("No guest found with ID: {} to delete", id);
                throw WeddingAppException.guestNotFound(id);
            }
            
            guestRepository.deleteById(id);
            log.info("Guest with ID: {} deleted successfully", id);
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error deleting guest with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }
}
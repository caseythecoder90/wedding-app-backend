package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.Guest;
import com.wedding.backend.wedding_app.repository.GuestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class GuestDao {

    private final GuestRepository guestRepository;

    public GuestDao(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(GuestDao.class);

    @Transactional
    public Guest saveGuest(
            String firstName,
            String lastName,
            String email,
            String phone,
            boolean plusOneAllowed) {

        log.info("GuestDao - Creating and saving guest to DB");

        Guest guest = Guest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .plusOneAllowed(plusOneAllowed)
                .build();

        try {
            Guest savedGuest = guestRepository.save(guest);
            log.info("Guest created and saved to DB => {}", savedGuest);
            return savedGuest;
        } catch (Exception e) {
            log.error("Exception while saving guest to DB!", e);
            throw new RuntimeException("Exception while saving guest to DB!", e);
        }
    }

    public Optional<Guest> fetchGuestByFullName(String firstName, String lastName) {
        log.info("GuestDao - Fetching guest with firstName={}, lastName={}", firstName, lastName);

        try {
            Optional<Guest> guestOpt =
                    guestRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);

            if (guestOpt.isPresent()) {
                log.info("Guest found: {}", guestOpt.get());
            } else {
                log.warn("Guest with firstName={}, lastName={} does not exist", firstName, lastName);
            }

            return guestOpt;
        } catch (Exception e) {
            log.error("Exception while fetching guest from DB!", e);
            throw new RuntimeException("Error fetching guest from database", e);
        }
    }

    @Transactional
    public Guest updateGuest(Guest guest) {
        log.info("GuestDao - Updating guest with ID: {}", guest.getId());

        if (guest.getId() == null) {
            log.error("Cannot update guest without ID");
            throw new IllegalArgumentException("Guest ID cannot be null for update operation");
        }

        try {
            Guest updatedGuest = guestRepository.save(guest);
            log.info("Guest updated successfully: {}", updatedGuest);
            return updatedGuest;
        } catch (Exception e) {
            log.error("Exception while updating guest!", e);
            throw new RuntimeException("Error updating guest", e);
        }
    }

    public List<Guest> fetchAllGuests() {
        log.info("GuestDao - Fetching all guests");

        try {
            List<Guest> guests = guestRepository.findAll();
            log.info("Found {} guests", guests.size());
            return guests;
        } catch (Exception e) {
            log.error("Exception while fetching all guests!", e);
            throw new RuntimeException("Error fetching all guests", e);
        }
    }

    @Transactional
    public void deleteGuest(Long id) {
        log.info("Deleting guest with ID: {}", id);

        try {
            if (guestRepository.existsById(id)) {
                guestRepository.deleteById(id);
                log.info("Guest with ID {} deleted successfully", id);
            } else {
                log.warn("No guest found with ID {} to delete", id);
                throw new EntityNotFoundException("Guest not found with ID: " + id);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception while deleting guest with ID: {}", id, e);
            throw new RuntimeException("Error deleting guest", e);
        }
    }
}

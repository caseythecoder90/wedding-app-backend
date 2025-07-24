package com.wedding.backend.wedding_app.dao;

import com.wedding.backend.wedding_app.entity.DonationEntity;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.repository.DonationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class DonationDao {

    private final DonationRepository donationRepository;
    private final Logger log = LoggerFactory.getLogger(DonationDao.class);

    public DonationDao(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    /**
     * Save a new donation
     * @param donation The donation entity to save
     * @return Saved donation entity
     */
    @Transactional
    public DonationEntity saveDonation(DonationEntity donation) {
        log.info("Saving donation from donor: {}", donation.getDonorName());

        try {
            DonationEntity savedDonation = donationRepository.save(donation);
            log.info("Donation saved successfully with ID: {}", savedDonation.getId());
            return savedDonation;
        } catch (Exception e) {
            log.error("Error saving donation to database", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find donation by ID
     * @param id The donation ID
     * @return Optional donation entity
     */
    @Transactional(readOnly = true)
    public Optional<DonationEntity> findDonationById(Long id) {
        log.info("Finding donation with ID: {}", id);

        try {
            Optional<DonationEntity> donation = donationRepository.findById(id);
            if (donation.isPresent()) {
                log.info("Donation found with ID: {}", id);
            } else {
                log.warn("No donation found with ID: {}", id);
            }
            return donation;
        } catch (Exception e) {
            log.error("Error finding donation with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get all donations ordered by donation date desc
     * @return List of all donations
     */
    @Transactional(readOnly = true)
    public List<DonationEntity> findAllDonations() {
        log.info("Fetching all donations");

        try {
            List<DonationEntity> donations = donationRepository.findAllByOrderByDonationDateDesc();
            log.info("Found {} donations", donations.size());
            return donations;
        } catch (Exception e) {
            log.error("Error fetching all donations", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find donations by status
     * @param status The donation status to filter by
     * @return List of donations with the specified status
     */
    @Transactional(readOnly = true)
    public List<DonationEntity> findDonationsByStatus(DonationStatus status) {
        log.info("Fetching donations with status: {}", status);

        try {
            List<DonationEntity> donations = donationRepository.findByStatusOrderByDonationDateDesc(status);
            log.info("Found {} donations with status: {}", donations.size(), status);
            return donations;
        } catch (Exception e) {
            log.error("Error fetching donations by status: {}", status, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Find confirmed donations without thank you emails sent
     * @return List of donations needing thank you emails
     */
    @Transactional(readOnly = true)
    public List<DonationEntity> findConfirmedDonationsWithoutThankYou() {
        log.info("Fetching confirmed donations without thank you emails");

        try {
            List<DonationEntity> donations = donationRepository.findConfirmedDonationsWithoutThankYou();
            log.info("Found {} donations needing thank you emails", donations.size());
            return donations;
        } catch (Exception e) {
            log.error("Error fetching donations needing thank you emails", e);
            throw WeddingAppException.databaseError();
        }
    }


    /**
     * Update an existing donation
     * @param donation The donation entity to update
     * @return Updated donation entity
     */
    @Transactional
    public DonationEntity updateDonation(DonationEntity donation) {
        log.info("Updating donation with ID: {}", donation.getId());

        try {
            // Ensure the donation exists
            if (donation.getId() == null || !donationRepository.existsById(donation.getId())) {
                log.error("Donation not found with ID: {}", donation.getId());
                throw WeddingAppException.donationNotFound(donation.getId());
            }

            DonationEntity updatedDonation = donationRepository.save(donation);
            log.info("Donation updated successfully with ID: {}", updatedDonation.getId());
            return updatedDonation;
        } catch (WeddingAppException e) {
            // Re-throw application exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error updating donation with ID: {}", donation.getId(), e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Check if donation exists by ID
     * @param id The donation ID
     * @return true if donation exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.info("Checking if donation exists with ID: {}", id);

        try {
            boolean exists = donationRepository.existsById(id);
            log.info("Donation with ID {} exists: {}", id, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if donation exists with ID: {}", id, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get total amount of confirmed donations
     * @return Total confirmed donation amount
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalConfirmedDonations() {
        log.info("Fetching total confirmed donations");

        try {
            BigDecimal total = donationRepository.getTotalConfirmedDonations();
            log.info("Total confirmed donations: {}", total);
            return total;
        } catch (Exception e) {
            log.error("Error fetching total confirmed donations", e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Count donations by status
     * @param status The donation status to count
     * @return Number of donations with the specified status
     */
    @Transactional(readOnly = true)
    public Integer countDonationsByStatus(DonationStatus status) {
        log.info("Counting donations with status: {}", status);

        try {
            Integer count = donationRepository.countDonationsByStatus(status);
            log.info("Found {} donations with status: {}", count, status);
            return count;
        } catch (Exception e) {
            log.error("Error counting donations by status: {}", status, e);
            throw WeddingAppException.databaseError();
        }
    }

    /**
     * Get average donation amount for confirmed donations
     * @return Average donation amount
     */
    @Transactional(readOnly = true)
    public BigDecimal getAverageDonationAmount() {
        log.info("Fetching average donation amount");

        try {
            BigDecimal average = donationRepository.getAverageDonationAmount();
            log.info("Average donation amount: {}", average);
            return average;
        } catch (Exception e) {
            log.error("Error fetching average donation amount", e);
            throw WeddingAppException.databaseError();
        }
    }
}
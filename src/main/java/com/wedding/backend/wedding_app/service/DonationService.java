package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.DonationDao;
import com.wedding.backend.wedding_app.dto.DonationRequestDTO;
import com.wedding.backend.wedding_app.dto.DonationResponseDTO;
import com.wedding.backend.wedding_app.entity.DonationEntity;
import com.wedding.backend.wedding_app.entity.GuestEntity;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.enums.PaymentMethod;
import com.wedding.backend.wedding_app.exception.WeddingAppException;
import com.wedding.backend.wedding_app.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class DonationService {

    private final DonationDao donationDao;
    private final EmailService emailService;
    private final GuestService guestService;

    /**
     * Submit a new donation
     */
    public DonationResponseDTO submitDonation(DonationRequestDTO request) {
        log.info("BEGIN - Processing donation submission from: {}", request.getDonorName());

        // Look up guest if guestId is provided
        GuestEntity guest = null;
        if (request.getGuestId() != null) {
            try {
                guest = guestService.findById(request.getGuestId());
            } catch (WeddingAppException e) {
                log.warn("Guest ID {} not found for donation, proceeding without guest link", request.getGuestId());
            }
        }

        DonationEntity donation = DonationEntity.builder()
                .donorName(request.getDonorName())
                 .donorEmail(request.getDonorEmail())
                .donorPhone(request.getDonorPhone())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentReference(request.getPaymentReference())
                .message(request.getMessage())
                .guest(guest)
                .donationDate(OffsetDateTime.now())
                .status(DonationStatus.PENDING)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        DonationEntity savedDonation = donationDao.saveDonation(donation);

        try {
            emailService.sendDonationConfirmationEmail(savedDonation);
        } catch (Exception e) {
            log.error("Failed to send donation confirmation email", e);
        }

        log.info("END - Donation submitted successfully with ID: {}", savedDonation.getId());
        return convertToDonationResponseDTO(savedDonation);
    }

    /**
     * Confirm a pending donation
     */
    public DonationResponseDTO confirmDonation(Long donationId) {
        log.info("BEGIN - Confirming donation with ID: {}", donationId);

        DonationEntity donation = donationDao.findDonationById(donationId)
                .orElseThrow(() -> WeddingAppException.donationNotFound(donationId));

        if (donation.getStatus() == DonationStatus.CONFIRMED) {
            log.warn("Donation {} is already confirmed", donationId);
            return convertToDonationResponseDTO(donation);
        }

        donation.setStatus(DonationStatus.CONFIRMED);
        donation.setConfirmedDate(OffsetDateTime.now());
        donation.setUpdatedAt(OffsetDateTime.now());

        DonationEntity confirmed = donationDao.updateDonation(donation);

        log.info("END - Donation confirmed successfully");
        return convertToDonationResponseDTO(confirmed);
    }

    /**
     * Get all donations with optional status filter
     */
    public List<DonationResponseDTO> getAllDonations(DonationStatus status) {
        log.info("BEGIN - Fetching donations with status filter: {}", status);

        List<DonationEntity> donations;
        if (status != null) {
            donations = donationDao.findDonationsByStatus(status);
        } else {
            donations = donationDao.findAllDonations();
        }

        List<DonationResponseDTO> result = donations.stream()
                .map(this::convertToDonationResponseDTO)
                .collect(Collectors.toList());

        log.info("END - Found {} donations", result.size());
        return result;
    }

    /**
     * Get donation by ID
     */
    public DonationResponseDTO getDonationById(Long donationId) {
        log.info("BEGIN - Fetching donation with ID: {}", donationId);

        DonationEntity donation = donationDao.findDonationById(donationId)
                .orElseThrow(() -> WeddingAppException.donationNotFound(donationId));

        log.info("END - Donation fetched successfully");
        return convertToDonationResponseDTO(donation);
    }

    /**
     * Send thank you email for a confirmed donation
     */
    public void sendThankYouEmail(Long donationId) {
        log.info("BEGIN - Sending thank you email for donation: {}", donationId);

        DonationEntity donation = donationDao.findDonationById(donationId)
                .orElseThrow(() -> WeddingAppException.donationNotFound(donationId));

        if (donation.getStatus() != DonationStatus.CONFIRMED) {
            throw WeddingAppException.validationError("Cannot send thank you for unconfirmed donation");
        }

        if (donation.getThankYouSentDate() != null) {
            throw WeddingAppException.validationError("Thank you email already sent for this donation");
        }

        // Send thank you email
        emailService.sendDonationThankYouEmail(donation);

        // Update thank you sent date (keep status as CONFIRMED)
        donation.setThankYouSentDate(OffsetDateTime.now());
        donation.setUpdatedAt(OffsetDateTime.now());
        donationDao.updateDonation(donation);

        log.info("END - Thank you email sent successfully");
    }

    /**
     * Get donations that need thank you emails sent
     */
    public List<DonationResponseDTO> getDonationsNeedingThankYou() {
        log.info("BEGIN - Fetching donations needing thank you emails");

        List<DonationEntity> donations = donationDao.findConfirmedDonationsWithoutThankYou();
        List<DonationResponseDTO> result = donations.stream()
                .map(this::convertToDonationResponseDTO)
                .collect(Collectors.toList());

        log.info("END - Found {} donations needing thank you emails", result.size());
        return result;
    }


    private DonationResponseDTO convertToDonationResponseDTO(DonationEntity entity) {
        return DonationResponseDTO.builder()
                .id(entity.getId())
                .donorName(entity.getDonorName())
                .donorEmail(entity.getDonorEmail())
                .donorPhone(entity.getDonorPhone())
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod())
                .paymentReference(entity.getPaymentReference())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .guestId(entity.getGuest() != null ? entity.getGuest().getId() : null)
                .donationDate(entity.getDonationDate())
                .confirmedDate(entity.getConfirmedDate())
                .thankYouSentDate(entity.getThankYouSentDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
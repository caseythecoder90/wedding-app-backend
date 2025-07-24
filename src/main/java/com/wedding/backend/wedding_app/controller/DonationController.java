package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.DonationApiDocs;
import com.wedding.backend.wedding_app.dto.DonationRequestDTO;
import com.wedding.backend.wedding_app.dto.DonationResponseDTO;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.service.DonationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/registry/donations")
@Tag(name = "Donation Management", description = "APIs for managing honeymoon registry donations")
public class DonationController {

    private final DonationService donationService;
    private final Logger log = LoggerFactory.getLogger(DonationController.class);

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    /**
     * Submit a new donation
     */
    @PostMapping
    @DonationApiDocs.SubmitDonation
    public ResponseEntity<DonationResponseDTO> submitDonation(
            @Valid @RequestBody DonationRequestDTO request) {
        log.info("BEGIN - POST /v1/api/registry/donations - Submitting donation from: {}", request.getDonorName());

        DonationResponseDTO response = donationService.submitDonation(request);

        log.info("END - Donation submitted successfully with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all donations with optional status filter
     */
    @GetMapping
    @DonationApiDocs.GetAllDonations
    public ResponseEntity<List<DonationResponseDTO>> getAllDonations(
            @Parameter(description = "Filter by donation status (optional)")
            @RequestParam(required = false) DonationStatus status) {
        log.info("BEGIN - GET /v1/api/registry/donations - Fetching donations with status filter: {}", status);

        List<DonationResponseDTO> donations = donationService.getAllDonations(status);

        log.info("END - Found {} donations", donations.size());
        return ResponseEntity.ok(donations);
    }

    /**
     * Get donation by ID
     */
    @GetMapping("/{id}")
    @DonationApiDocs.GetDonationById
    public ResponseEntity<DonationResponseDTO> getDonationById(
            @Parameter(description = "Donation ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - GET /v1/api/registry/donations/{} - Fetching donation", id);

        DonationResponseDTO donation = donationService.getDonationById(id);

        log.info("END - Donation fetched successfully");
        return ResponseEntity.ok(donation);
    }

    /**
     * Confirm a pending donation (admin only)
     */
    @PutMapping("/{id}/confirm")
    @DonationApiDocs.ConfirmDonation
    public ResponseEntity<DonationResponseDTO> confirmDonation(
            @Parameter(description = "Donation ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - PUT /v1/api/registry/donations/{}/confirm - Confirming donation", id);

        DonationResponseDTO response = donationService.confirmDonation(id);

        log.info("END - Donation confirmed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Send thank you email for a donation (admin only)
     */
    @PostMapping("/{id}/send-thank-you")
    @DonationApiDocs.SendThankYouEmail
    public ResponseEntity<Void> sendThankYouEmail(
            @Parameter(description = "Donation ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - POST /v1/api/registry/donations/{}/send-thank-you - Sending thank you email", id);

        donationService.sendThankYouEmail(id);

        log.info("END - Thank you email sent successfully");
        return ResponseEntity.ok().build();
    }

    /**
     * Get donations that need thank you emails (admin only)
     */
    @GetMapping("/pending-thank-you")
    @DonationApiDocs.GetDonationsNeedingThankYou
    public ResponseEntity<List<DonationResponseDTO>> getDonationsNeedingThankYou() {
        log.info("BEGIN - GET /v1/api/registry/donations/pending-thank-you - Fetching donations needing thank you");

        List<DonationResponseDTO> donations = donationService.getDonationsNeedingThankYou();

        log.info("END - Found {} donations needing thank you emails", donations.size());
        return ResponseEntity.ok(donations);
    }
}
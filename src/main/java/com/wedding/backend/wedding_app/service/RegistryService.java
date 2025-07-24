package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dao.DonationDao;
import com.wedding.backend.wedding_app.dto.RegistryOverviewDTO;
import com.wedding.backend.wedding_app.dto.RegistrySettingsDTO;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class RegistryService {

    private final DonationDao donationDao;
    private final RegistrySettingsService registrySettingsService;
    private final Logger log = LoggerFactory.getLogger(RegistryService.class);

    public RegistryService(DonationDao donationDao,
                           RegistrySettingsService registrySettingsService) {
        this.donationDao = donationDao;
        this.registrySettingsService = registrySettingsService;
    }

    /**
     * Get complete registry overview with progress and statistics
     */
    public RegistryOverviewDTO getRegistryOverview() {
        log.info("BEGIN - Fetching registry overview");

        RegistrySettingsDTO settings = registrySettingsService.getActiveRegistrySettings();
        BigDecimal totalDonated = donationDao.getTotalConfirmedDonations();
        Integer totalDonations = donationDao.countDonationsByStatus(DonationStatus.CONFIRMED);
        Integer pendingDonations = donationDao.countDonationsByStatus(DonationStatus.PENDING);
        BigDecimal averageDonation = donationDao.getAverageDonationAmount();

        Double progressPercentage = calculateProgressPercentage(totalDonated, settings.getHoneymoonGoalAmount());

        RegistryOverviewDTO overview = RegistryOverviewDTO.builder()
                .settings(settings)
                .totalDonated(Objects.nonNull(totalDonated) ? totalDonated : BigDecimal.ZERO)
                .goalAmount(settings.getHoneymoonGoalAmount())
                .progressPercentage(progressPercentage)
                .totalDonations(Objects.nonNull(totalDonations) ? totalDonations : 0)
                .pendingDonations(Objects.nonNull(pendingDonations) ? pendingDonations : 0)
                .averageDonation(Objects.nonNull(averageDonation) ? averageDonation : BigDecimal.ZERO)
                .build();

        log.info("END - Registry overview fetched successfully. Progress: {}%", progressPercentage);
        return overview;
    }

    private Double calculateProgressPercentage(BigDecimal totalDonated, BigDecimal goalAmount) {
        if (Objects.isNull(goalAmount) || goalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }

        if (Objects.isNull(totalDonated)) {
            return 0.0;
        }

        return totalDonated.divide(goalAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}
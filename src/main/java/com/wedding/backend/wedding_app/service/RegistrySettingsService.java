// RegistrySettingsService.java
package com.wedding.backend.wedding_app.service;

import com.wedding.backend.wedding_app.dto.RegistrySettingsDTO;
import com.wedding.backend.wedding_app.dto.RegistrySettingsRequestDTO;
import com.wedding.backend.wedding_app.entity.RegistrySettingsEntity;
import com.wedding.backend.wedding_app.repository.RegistrySettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class RegistrySettingsService {

    private final RegistrySettingsRepository registrySettingsRepository;
    private final Logger log = LoggerFactory.getLogger(RegistrySettingsService.class);

    public RegistrySettingsService(RegistrySettingsRepository registrySettingsRepository) {
        this.registrySettingsRepository = registrySettingsRepository;
    }

    /**
     * Get the active registry settings (cached for 1 hour)
     * This will be the primary method called by frontend
     */
    @Cacheable(value = "registrySettings", key = "'active'")
    public RegistrySettingsDTO getActiveRegistrySettings() {
        log.info("BEGIN - Fetching active registry settings");

        RegistrySettingsEntity settings = registrySettingsRepository.findActiveSettings()
                .orElseGet(this::createDefaultSettings);

        log.info("END - Registry settings fetched successfully");
        return convertToDTO(settings);
    }

    /**
     * Update registry settings and evict cache
     */
    @CacheEvict(value = "registrySettings", key = "'active'")
    public RegistrySettingsDTO updateRegistrySettings(RegistrySettingsRequestDTO request) {
        log.info("BEGIN - Updating registry settings");

        RegistrySettingsEntity settings = registrySettingsRepository.findActiveSettings()
                .orElseGet(this::createDefaultSettings);

        // Update fields
        settings.setHoneymoonGoalAmount(request.getHoneymoonGoalAmount());
        settings.setVenmoHandle(request.getVenmoHandle());
        settings.setZelleHandle(request.getZelleHandle());
        settings.setRegistryDescription(request.getRegistryDescription());
        settings.setUpdatedAt(OffsetDateTime.now());

        RegistrySettingsEntity saved = registrySettingsRepository.save(settings);

        log.info("END - Registry settings updated successfully");
        return convertToDTO(saved);
    }

    /**
     * Toggle registry active status and evict cache
     */
    @CacheEvict(value = "registrySettings", key = "'active'")
    public RegistrySettingsDTO toggleRegistryStatus(boolean isActive) {
        log.info("BEGIN - Toggling registry status to: {}", isActive);

        RegistrySettingsEntity settings = registrySettingsRepository.findActiveSettings()
                .orElseGet(this::createDefaultSettings);

        settings.setIsActive(isActive);
        settings.setUpdatedAt(OffsetDateTime.now());
        RegistrySettingsEntity saved = registrySettingsRepository.save(settings);

        log.info("END - Registry status toggled successfully");
        return convertToDTO(saved);
    }

    /**
     * Create default settings if none exist (called automatically)
     */
    private RegistrySettingsEntity createDefaultSettings() {
        log.info("Creating default registry settings");

        RegistrySettingsEntity defaultSettings = RegistrySettingsEntity.builder()
                .honeymoonGoalAmount(BigDecimal.valueOf(5000.00))
                .venmoHandle("@casey-and-yasmim")
                .zelleHandle("casey.yasmim@email.com")
                .registryDescription("Help us create amazing honeymoon memories!")
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        return registrySettingsRepository.save(defaultSettings);
    }

    private RegistrySettingsDTO convertToDTO(RegistrySettingsEntity entity) {
        return RegistrySettingsDTO.builder()
                .id(entity.getId())
                .honeymoonGoalAmount(entity.getHoneymoonGoalAmount())
                .venmoHandle(entity.getVenmoHandle())
                .zelleHandle(entity.getZelleHandle())
                .registryDescription(entity.getRegistryDescription())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
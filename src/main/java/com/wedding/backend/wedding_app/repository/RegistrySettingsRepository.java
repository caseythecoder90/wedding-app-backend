package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.RegistrySettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrySettingsRepository extends JpaRepository<RegistrySettingsEntity, Long> {

    /**
     * Find the currently active registry settings
     * There should only be one active registry at a time
     */
    @Query("SELECT rs FROM RegistrySettingsEntity rs WHERE rs.isActive = true")
    Optional<RegistrySettingsEntity> findActiveSettings();

    /**
     * Count how many active registry settings exist (should be 0 or 1)
     */
    @Query("SELECT COUNT(rs) FROM RegistrySettingsEntity rs WHERE rs.isActive = true")
    int countActiveSettings();

    /**
     * Check if any active registry exists
     */
    @Query("SELECT CASE WHEN COUNT(rs) > 0 THEN true ELSE false END FROM RegistrySettingsEntity rs WHERE rs.isActive = true")
    boolean existsActiveRegistry();
}
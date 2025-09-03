package com.wedding.backend.wedding_app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "registry_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrySettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "honeymoon_goal_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal honeymoonGoalAmount;

    @Column(name = "venmo_handle")
    private String venmoHandle;

    @Column(name = "zelle_handle")
    private String zelleHandle;

    @Column(name = "registry_description")
    private String registryDescription;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}
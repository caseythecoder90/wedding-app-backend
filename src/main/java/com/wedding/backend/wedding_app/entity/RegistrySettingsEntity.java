package com.wedding.backend.wedding_app.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "registry_settings")
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
    private Boolean isActive = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public RegistrySettingsEntity() {}

    private RegistrySettingsEntity(Builder builder) {
        this.honeymoonGoalAmount = builder.honeymoonGoalAmount;
        this.venmoHandle = builder.venmoHandle;
        this.zelleHandle = builder.zelleHandle;
        this.registryDescription = builder.registryDescription;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal honeymoonGoalAmount;
        private String venmoHandle;
        private String zelleHandle;
        private String registryDescription;
        private Boolean isActive;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public Builder honeymoonGoalAmount(BigDecimal honeymoonGoalAmount) {
            this.honeymoonGoalAmount = honeymoonGoalAmount;
            return this;
        }

        public Builder venmoHandle(String venmoHandle) {
            this.venmoHandle = venmoHandle;
            return this;
        }

        public Builder zelleHandle(String zelleHandle) {
            this.zelleHandle = zelleHandle;
            return this;
        }

        public Builder registryDescription(String registryDescription) {
            this.registryDescription = registryDescription;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RegistrySettingsEntity build() {
            return new RegistrySettingsEntity(this);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getHoneymoonGoalAmount() {
        return honeymoonGoalAmount;
    }

    public void setHoneymoonGoalAmount(BigDecimal honeymoonGoalAmount) {
        this.honeymoonGoalAmount = honeymoonGoalAmount;
    }

    public String getVenmoHandle() {
        return venmoHandle;
    }

    public void setVenmoHandle(String venmoHandle) {
        this.venmoHandle = venmoHandle;
    }

    public String getZelleHandle() {
        return zelleHandle;
    }

    public void setZelleHandle(String zelleHandle) {
        this.zelleHandle = zelleHandle;
    }

    public String getRegistryDescription() {
        return registryDescription;
    }

    public void setRegistryDescription(String registryDescription) {
        this.registryDescription = registryDescription;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrySettingsEntity that = (RegistrySettingsEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
                RegistrySettings:
                    id: %s
                    honeymoonGoalAmount: %s
                    venmoHandle: %s
                    zelleHandle: %s
                    isActive: %s
                """.formatted(id, honeymoonGoalAmount, venmoHandle, zelleHandle, isActive);
    }
}
package com.wedding.backend.wedding_app.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class RegistrySettingsDTO {
    private Long id;
    private BigDecimal honeymoonGoalAmount;
    private String venmoHandle;
    private String zelleHandle;
    private String registryDescription;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public RegistrySettingsDTO() {}

    private RegistrySettingsDTO(Builder builder) {
        this.id = builder.id;
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
        private Long id;
        private BigDecimal honeymoonGoalAmount;
        private String venmoHandle;
        private String zelleHandle;
        private String registryDescription;
        private Boolean isActive;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public RegistrySettingsDTO build() {
            return new RegistrySettingsDTO(this);
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

    @Override
    public String toString() {
        return """
                RegistrySettingsDTO:
                    id: %s
                    honeymoonGoalAmount: %s
                    venmoHandle: %s
                    zelleHandle: %s
                    isActive: %s
                """.formatted(id, honeymoonGoalAmount, venmoHandle, zelleHandle, isActive);
    }
}

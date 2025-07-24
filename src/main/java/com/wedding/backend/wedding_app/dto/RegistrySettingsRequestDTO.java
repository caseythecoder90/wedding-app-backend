package com.wedding.backend.wedding_app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class RegistrySettingsRequestDTO {

    @NotNull(message = "Honeymoon goal amount is required")
    @DecimalMin(value = "100.00", message = "Minimum goal amount is $100.00")
    private BigDecimal honeymoonGoalAmount;

    @Size(max = 255, message = "Venmo handle must be less than 255 characters")
    private String venmoHandle;

    @Size(max = 255, message = "Zelle handle must be less than 255 characters")
    private String zelleHandle;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String registryDescription;

    public RegistrySettingsRequestDTO() {}

    private RegistrySettingsRequestDTO(Builder builder) {
        this.honeymoonGoalAmount = builder.honeymoonGoalAmount;
        this.venmoHandle = builder.venmoHandle;
        this.zelleHandle = builder.zelleHandle;
        this.registryDescription = builder.registryDescription;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal honeymoonGoalAmount;
        private String venmoHandle;
        private String zelleHandle;
        private String registryDescription;

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

        public RegistrySettingsRequestDTO build() {
            return new RegistrySettingsRequestDTO(this);
        }
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return """
                RegistrySettingsRequestDTO:
                    honeymoonGoalAmount: %s
                    venmoHandle: %s
                    zelleHandle: %s
                    registryDescription: %s
                """.formatted(honeymoonGoalAmount, venmoHandle, zelleHandle, registryDescription);
    }
}

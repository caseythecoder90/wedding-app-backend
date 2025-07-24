package com.wedding.backend.wedding_app.dto;

import java.math.BigDecimal;

public class RegistryOverviewDTO {
    private RegistrySettingsDTO settings;
    private BigDecimal totalDonated;
    private BigDecimal goalAmount;
    private Double progressPercentage;
    private Integer totalDonations;
    private Integer pendingDonations;
    private BigDecimal averageDonation;

    public RegistryOverviewDTO() {}

    private RegistryOverviewDTO(Builder builder) {
        this.settings = builder.settings;
        this.totalDonated = builder.totalDonated;
        this.goalAmount = builder.goalAmount;
        this.progressPercentage = builder.progressPercentage;
        this.totalDonations = builder.totalDonations;
        this.pendingDonations = builder.pendingDonations;
        this.averageDonation = builder.averageDonation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RegistrySettingsDTO settings;
        private BigDecimal totalDonated;
        private BigDecimal goalAmount;
        private Double progressPercentage;
        private Integer totalDonations;
        private Integer pendingDonations;
        private BigDecimal averageDonation;

        public Builder settings(RegistrySettingsDTO settings) {
            this.settings = settings;
            return this;
        }

        public Builder totalDonated(BigDecimal totalDonated) {
            this.totalDonated = totalDonated;
            return this;
        }

        public Builder goalAmount(BigDecimal goalAmount) {
            this.goalAmount = goalAmount;
            return this;
        }

        public Builder progressPercentage(Double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }

        public Builder totalDonations(Integer totalDonations) {
            this.totalDonations = totalDonations;
            return this;
        }

        public Builder pendingDonations(Integer pendingDonations) {
            this.pendingDonations = pendingDonations;
            return this;
        }

        public Builder averageDonation(BigDecimal averageDonation) {
            this.averageDonation = averageDonation;
            return this;
        }

        public RegistryOverviewDTO build() {
            return new RegistryOverviewDTO(this);
        }
    }

    // Getters and Setters
    public RegistrySettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(RegistrySettingsDTO settings) {
        this.settings = settings;
    }

    public BigDecimal getTotalDonated() {
        return totalDonated;
    }

    public void setTotalDonated(BigDecimal totalDonated) {
        this.totalDonated = totalDonated;
    }

    public BigDecimal getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(BigDecimal goalAmount) {
        this.goalAmount = goalAmount;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getTotalDonations() {
        return totalDonations;
    }

    public void setTotalDonations(Integer totalDonations) {
        this.totalDonations = totalDonations;
    }

    public Integer getPendingDonations() {
        return pendingDonations;
    }

    public void setPendingDonations(Integer pendingDonations) {
        this.pendingDonations = pendingDonations;
    }

    public BigDecimal getAverageDonation() {
        return averageDonation;
    }

    public void setAverageDonation(BigDecimal averageDonation) {
        this.averageDonation = averageDonation;
    }

    @Override
    public String toString() {
        return """
                RegistryOverviewDTO:
                    totalDonated: %s
                    goalAmount: %s
                    progressPercentage: %s%%
                    totalDonations: %s
                    pendingDonations: %s
                """.formatted(totalDonated, goalAmount, progressPercentage, totalDonations, pendingDonations);
    }
}
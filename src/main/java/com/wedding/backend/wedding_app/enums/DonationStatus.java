package com.wedding.backend.wedding_app.enums;

public enum DonationStatus {
    PENDING("Pending Confirmation"),
    CONFIRMED("Confirmed");

    private final String displayName;

    DonationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

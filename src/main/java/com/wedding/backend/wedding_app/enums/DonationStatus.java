package com.wedding.backend.wedding_app.enums;

import lombok.Getter;

@Getter
public enum DonationStatus {
    PENDING("Pending Confirmation"),
    CONFIRMED("Confirmed"),
    THANKED("Thank You Sent");

    private final String displayName;

    DonationStatus(String displayName) {
        this.displayName = displayName;
    }

}

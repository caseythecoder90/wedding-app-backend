package com.wedding.backend.wedding_app.dto;

import java.time.OffsetDateTime;

public class RSVPRequestDTO {
    private Long guestId;
    private Boolean attending;
    private Boolean bringingPlusOne;
    private String plusOneName;
    private String dietaryRestrictions;
    private String email;
    private boolean sendConfirmationEmail;
    private OffsetDateTime submittedAt;

    public Long getGuestId() {
        return guestId;
    }

    public Boolean getAttending() {
        return attending;
    }

    public Boolean getBringingPlusOne() {
        return bringingPlusOne;
    }

    public String getPlusOneName() {
        return plusOneName;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSendConfirmationEmail() {
        return sendConfirmationEmail;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    private RSVPRequestDTO(Builder builder) {
        this.guestId = builder.guestId;
        this.attending = builder.attending;
        this.bringingPlusOne = builder.bringingPlusOne;
        this.plusOneName = builder.plusOneName;
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.email = builder.email;
        this.sendConfirmationEmail = builder.sendConfirmationEmail;
        this.submittedAt = builder.submittedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long guestId;
        private Boolean attending;
        private Boolean bringingPlusOne;
        private String plusOneName;
        private String dietaryRestrictions;
        private String email;
        private boolean sendConfirmationEmail;
        private OffsetDateTime submittedAt;

        public Builder guestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }

        public Builder attending(Boolean attending) {
            this.attending = attending;
            return this;
        }

        public Builder bringingPlusOne(Boolean bringingPlusOne) {
            this.bringingPlusOne = bringingPlusOne;
            return this;
        }

        public Builder plusOneName(String plusOneName) {
            this.plusOneName = plusOneName;
            return this;
        }

        public Builder dietaryRestrictions(String dietaryRestrictions) {
            this.dietaryRestrictions = dietaryRestrictions;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder sendConfirmationEmail(boolean sendConfirmationEmail) {
            this.sendConfirmationEmail = sendConfirmationEmail;
            return this;
        }

        public Builder submittedAt(OffsetDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public RSVPRequestDTO build() {
            return new RSVPRequestDTO(this);
        }
    }
}
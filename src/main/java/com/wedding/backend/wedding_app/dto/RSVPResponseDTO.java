package com.wedding.backend.wedding_app.dto;

import java.time.OffsetDateTime;

public class RSVPResponseDTO {
    private Long id;
    private Long guestId;
    private String guestName;
    private String guestEmail;
    private Boolean attending;
    private Boolean bringingPlusOne;
    private String plusOneName;
    private String dietaryRestrictions;
    private OffsetDateTime submittedAt;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGuestId() {
        return guestId;
    }
    
    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }
    
    public Boolean getAttending() {
        return attending;
    }
    
    public void setAttending(Boolean attending) {
        this.attending = attending;
    }
    
    public Boolean getBringingPlusOne() {
        return bringingPlusOne;
    }
    
    public void setBringingPlusOne(Boolean bringingPlusOne) {
        this.bringingPlusOne = bringingPlusOne;
    }
    
    public String getPlusOneName() {
        return plusOneName;
    }
    
    public void setPlusOneName(String plusOneName) {
        this.plusOneName = plusOneName;
    }
    
    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }
    
    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
    
    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    private RSVPResponseDTO(Builder builder) {
        this.id = builder.id;
        this.guestId = builder.guestId;
        this.guestName = builder.guestName;
        this.guestEmail = builder.guestEmail;
        this.attending = builder.attending;
        this.bringingPlusOne = builder.bringingPlusOne;
        this.plusOneName = builder.plusOneName;
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.submittedAt = builder.submittedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long guestId;
        private String guestName;
        private String guestEmail;
        private Boolean attending;
        private Boolean bringingPlusOne;
        private String plusOneName;
        private String dietaryRestrictions;
        private OffsetDateTime submittedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder guestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }

        public Builder guestName(String guestName) {
            this.guestName = guestName;
            return this;
        }

        public Builder guestEmail(String guestEmail) {
            this.guestEmail = guestEmail;
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

        public Builder submittedAt(OffsetDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public RSVPResponseDTO build() {
            return new RSVPResponseDTO(this);
        }
    }
}
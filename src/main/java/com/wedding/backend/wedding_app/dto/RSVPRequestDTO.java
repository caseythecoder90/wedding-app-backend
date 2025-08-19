package com.wedding.backend.wedding_app.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@JsonDeserialize(builder = RSVPRequestDTO.Builder.class)
public class RSVPRequestDTO {
    private final Long guestId;
    private final Boolean attending;
    private final Boolean bringingPlusOne;
    private final String plusOneName;
    private final String dietaryRestrictions;
    private final String email;
    private final boolean sendConfirmationEmail;
    private final OffsetDateTime submittedAt;
    
    // Family-related fields
    private final List<FamilyMemberRSVPRequest> familyMembers;

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
    
    public List<FamilyMemberRSVPRequest> getFamilyMembers() {
        return familyMembers;
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
        this.familyMembers = builder.familyMembers;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Long guestId;
        private Boolean attending;
        private Boolean bringingPlusOne;
        private String plusOneName;
        private String dietaryRestrictions;
        private String email;
        private boolean sendConfirmationEmail;
        private OffsetDateTime submittedAt;
        private List<FamilyMemberRSVPRequest> familyMembers;

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
        
        public Builder familyMembers(List<FamilyMemberRSVPRequest> familyMembers) {
            this.familyMembers = familyMembers;
            return this;
        }

        public RSVPRequestDTO build() {
            return new RSVPRequestDTO(this);
        }
    }
    
    // Nested class for family member RSVP data
    public static class FamilyMemberRSVPRequest {
        private Long familyMemberId; // null for new family members
        private String firstName;
        private String lastName;
        private String ageGroup;
        private Boolean isAttending;
        private String dietaryRestrictions;
        
        // Default constructor for Jackson
        public FamilyMemberRSVPRequest() {}
        
        // Getters and Setters
        public Long getFamilyMemberId() {
            return familyMemberId;
        }
        
        public void setFamilyMemberId(Long familyMemberId) {
            this.familyMemberId = familyMemberId;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public String getAgeGroup() {
            return ageGroup;
        }
        
        public void setAgeGroup(String ageGroup) {
            this.ageGroup = ageGroup;
        }
        
        public Boolean getIsAttending() {
            return isAttending;
        }
        
        public void setIsAttending(Boolean isAttending) {
            this.isAttending = isAttending;
        }
        
        public String getDietaryRestrictions() {
            return dietaryRestrictions;
        }
        
        public void setDietaryRestrictions(String dietaryRestrictions) {
            this.dietaryRestrictions = dietaryRestrictions;
        }
        
        @Override
        public String toString() {
            return "FamilyMemberRSVPRequest{" +
                    "familyMemberId=" + familyMemberId +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", ageGroup='" + ageGroup + '\'' +
                    ", isAttending=" + isAttending +
                    ", dietaryRestrictions='" + dietaryRestrictions + '\'' +
                    '}';
        }
    }
}
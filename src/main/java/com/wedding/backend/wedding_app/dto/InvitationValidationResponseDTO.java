package com.wedding.backend.wedding_app.dto;

import java.util.List;

public class InvitationValidationResponseDTO {
    private GuestResponseDTO guest;
    private RSVPResponseDTO existingRsvp;
    private boolean hasExistingRsvp;
    
    // Family-related fields
    private FamilyGroupResponseDTO familyGroup;
    private List<FamilyMemberResponseDTO> familyMembers;
    private boolean isFamily;
    
    // Default constructor
    public InvitationValidationResponseDTO() {
    }
    
    // Constructor with all fields
    private InvitationValidationResponseDTO(Builder builder) {
        this.guest = builder.guest;
        this.existingRsvp = builder.existingRsvp;
        this.hasExistingRsvp = builder.hasExistingRsvp;
        this.familyGroup = builder.familyGroup;
        this.familyMembers = builder.familyMembers;
        this.isFamily = builder.isFamily;
    }
    
    // Getters and Setters
    public GuestResponseDTO getGuest() {
        return guest;
    }
    
    public void setGuest(GuestResponseDTO guest) {
        this.guest = guest;
    }
    
    public RSVPResponseDTO getExistingRsvp() {
        return existingRsvp;
    }
    
    public void setExistingRsvp(RSVPResponseDTO existingRsvp) {
        this.existingRsvp = existingRsvp;
    }
    
    public boolean isHasExistingRsvp() {
        return hasExistingRsvp;
    }
    
    public void setHasExistingRsvp(boolean hasExistingRsvp) {
        this.hasExistingRsvp = hasExistingRsvp;
    }
    
    public FamilyGroupResponseDTO getFamilyGroup() {
        return familyGroup;
    }
    
    public void setFamilyGroup(FamilyGroupResponseDTO familyGroup) {
        this.familyGroup = familyGroup;
    }
    
    public List<FamilyMemberResponseDTO> getFamilyMembers() {
        return familyMembers;
    }
    
    public void setFamilyMembers(List<FamilyMemberResponseDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }
    
    public boolean isFamily() {
        return isFamily;
    }
    
    public void setFamily(boolean family) {
        isFamily = family;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private GuestResponseDTO guest;
        private RSVPResponseDTO existingRsvp;
        private boolean hasExistingRsvp;
        private FamilyGroupResponseDTO familyGroup;
        private List<FamilyMemberResponseDTO> familyMembers;
        private boolean isFamily;
        
        public Builder guest(GuestResponseDTO guest) {
            this.guest = guest;
            return this;
        }
        
        public Builder existingRsvp(RSVPResponseDTO existingRsvp) {
            this.existingRsvp = existingRsvp;
            return this;
        }
        
        public Builder hasExistingRsvp(boolean hasExistingRsvp) {
            this.hasExistingRsvp = hasExistingRsvp;
            return this;
        }
        
        public Builder familyGroup(FamilyGroupResponseDTO familyGroup) {
            this.familyGroup = familyGroup;
            return this;
        }
        
        public Builder familyMembers(List<FamilyMemberResponseDTO> familyMembers) {
            this.familyMembers = familyMembers;
            return this;
        }
        
        public Builder isFamily(boolean isFamily) {
            this.isFamily = isFamily;
            return this;
        }
        
        public InvitationValidationResponseDTO build() {
            return new InvitationValidationResponseDTO(this);
        }
    }
    
    @Override
    public String toString() {
        return "InvitationValidationResponseDTO{" +
                "guest=" + guest +
                ", existingRsvp=" + existingRsvp +
                ", hasExistingRsvp=" + hasExistingRsvp +
                ", familyGroup=" + familyGroup +
                ", familyMembers=" + familyMembers +
                ", isFamily=" + isFamily +
                '}';
    }
}
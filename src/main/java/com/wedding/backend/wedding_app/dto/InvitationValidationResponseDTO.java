package com.wedding.backend.wedding_app.dto;


public class InvitationValidationResponseDTO {
    private GuestResponseDTO guest;
    private RSVPResponseDTO existingRsvp;
    private boolean hasExistingRsvp;
    
    // Default constructor
    public InvitationValidationResponseDTO() {
    }
    
    // Constructor with all fields
    private InvitationValidationResponseDTO(Builder builder) {
        this.guest = builder.guest;
        this.existingRsvp = builder.existingRsvp;
        this.hasExistingRsvp = builder.hasExistingRsvp;
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
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private GuestResponseDTO guest;
        private RSVPResponseDTO existingRsvp;
        private boolean hasExistingRsvp;
        
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
                '}';
    }
}
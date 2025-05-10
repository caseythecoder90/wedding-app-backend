package com.wedding.backend.wedding_app.dto;

public class GuestResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean plusOneAllowed;
    private Boolean hasRsvp;
    private Long rsvpId;

    private GuestResponseDTO(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.plusOneAllowed = builder.plusOneAllowed;
        this.hasRsvp = builder.hasRsvp;
        this.rsvpId = builder.rsvpId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Boolean plusOneAllowed;
        private Boolean hasRsvp;
        private Long rsvpId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder plusOneAllowed(Boolean plusOneAllowed) {
            this.plusOneAllowed = plusOneAllowed;
            return this;
        }

        public Builder hasRsvp(Boolean hasRsvp) {
            this.hasRsvp = hasRsvp;
            return this;
        }

        public Builder rsvpId(Long rsvpId) {
            this.rsvpId = rsvpId;
            return this;
        }

        public GuestResponseDTO build() {
            return new GuestResponseDTO(this);
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPlusOneAllowed() {
        return plusOneAllowed;
    }

    public void setPlusOneAllowed(Boolean plusOneAllowed) {
        this.plusOneAllowed = plusOneAllowed;
    }

    public Boolean getHasRsvp() {
        return hasRsvp;
    }

    public void setHasRsvp(Boolean hasRsvp) {
        this.hasRsvp = hasRsvp;
    }

    public Long getRsvpId() {
        return rsvpId;
    }

    public void setRsvpId(Long rsvpId) {
        this.rsvpId = rsvpId;
    }
    
    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        GuestResponseDTO that = (GuestResponseDTO) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

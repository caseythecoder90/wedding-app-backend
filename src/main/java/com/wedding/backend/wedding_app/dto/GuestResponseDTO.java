package com.wedding.backend.wedding_app.dto;

import lombok.Data;

@Data
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
}

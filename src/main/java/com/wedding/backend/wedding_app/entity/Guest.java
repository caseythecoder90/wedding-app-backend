package com.wedding.backend.wedding_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean plusOneAllowed;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    private RSVP rsvp;

    public Guest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // Private constructor used by Builder
    private Guest(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.plusOneAllowed = builder.plusOneAllowed;
        this.rsvp = builder.rsvp;
    }

    public Boolean getPlusOneAllowed() {
        return plusOneAllowed;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public RSVP getRsvp() {
        return rsvp;
    }

    // Builder class
    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private Boolean plusOneAllowed;
        private RSVP rsvp;

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

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder plusOneAllowed(Boolean plusOneAllowed) {
            this.plusOneAllowed = plusOneAllowed;
            return this;
        }

        public Builder rsvp(RSVP rsvp) {
            this.rsvp = rsvp;
            return this;
        }

        public Guest build() {
            return new Guest(this);
        }
    }
}

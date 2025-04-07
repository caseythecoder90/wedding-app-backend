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
public class GuestEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean plusOneAllowed;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    private RSVPEntity rsvp;

    public GuestEntity() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // Private constructor used by Builder
    private GuestEntity(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.plusOneAllowed = builder.plusOneAllowed;
        this.rsvp = builder.rsvp;
    }

    // Builder class
    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private Boolean plusOneAllowed;
        private RSVPEntity rsvp;

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

        public Builder rsvp(RSVPEntity rsvp) {
            this.rsvp = rsvp;
            return this;
        }

        public GuestEntity build() {
            return new GuestEntity(this);
        }
    }

    @Override
    public String toString() {
        return """
                Guest:
                    firstName: %s
                    lastName: %s
                    email: %s
                    phone: %s
                """.formatted(firstName, lastName, email, phone);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getPlusOneAllowed() {
        return plusOneAllowed;
    }

    public void setPlusOneAllowed(Boolean plusOneAllowed) {
        this.plusOneAllowed = plusOneAllowed;
    }

    public RSVPEntity getRsvp() {
        return rsvp;
    }

    public void setRsvp(RSVPEntity rsvp) {
        this.rsvp = rsvp;
    }
    
    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        GuestEntity that = (GuestEntity) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

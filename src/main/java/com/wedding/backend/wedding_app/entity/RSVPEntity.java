package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import java.time.OffsetDateTime;

@Entity
@Table(name = "rsvps")
public class RSVPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "guest_id")
    @JsonBackReference
    private GuestEntity guest;

    private Boolean attending;
    private Boolean bringingPlusOne;
    private String plusOneName;
    private String dietaryRestrictions;
    private OffsetDateTime submittedAt;

    public RSVPEntity() {}

    private RSVPEntity(Builder builder) {
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
        private Boolean attending;
        private Boolean bringingPlusOne;
        private String plusOneName;
        private String dietaryRestrictions;
        private OffsetDateTime submittedAt;

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

        public RSVPEntity build() {
            return new RSVPEntity(this);
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
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
    
    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RSVPEntity that = (RSVPEntity) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "RSVPEntity{" +
                "id=" + id +
                ", guest=" + (guest != null ? guest.getId() : null) +
                ", attending=" + attending +
                ", bringingPlusOne=" + bringingPlusOne +
                ", plusOneName='" + plusOneName + '\'' +
                ", dietaryRestrictions='" + dietaryRestrictions + '\'' +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
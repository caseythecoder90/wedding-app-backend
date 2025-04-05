package com.wedding.backend.wedding_app.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "rsvps")
public class RSVPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "guest_id")
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

    public Builder builder() {
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
}
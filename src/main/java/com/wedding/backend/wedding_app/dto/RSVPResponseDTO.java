package com.wedding.backend.wedding_app.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class RSVPResponseDTO {
    private Long id;
    private Long guestId;
    private String guestName;
    private Boolean attending;
    private Boolean bringingPlusOne;
    private String plusOneName;
    private String dietaryRestrictions;
    private OffsetDateTime submittedAt;

    private RSVPResponseDTO(Builder builder) {
        this.id = builder.id;
        this.guestId = builder.guestId;
        this.guestName = builder.guestName;
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
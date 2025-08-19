package com.wedding.backend.wedding_app.dto;

public class FamilyMemberResponseDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String ageGroup;
    private final String dietaryRestrictions;
    private final Boolean isAttending;
    private final Long familyGroupId;

    public FamilyMemberResponseDTO(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.ageGroup = builder.ageGroup;
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.isAttending = builder.isAttending;
        this.familyGroupId = builder.familyGroupId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String ageGroup;
        private String dietaryRestrictions;
        private Boolean isAttending;
        private Long familyGroupId;

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

        public Builder ageGroup(String ageGroup) {
            this.ageGroup = ageGroup;
            return this;
        }

        public Builder dietaryRestrictions(String dietaryRestrictions) {
            this.dietaryRestrictions = dietaryRestrictions;
            return this;
        }

        public Builder isAttending(Boolean isAttending) {
            this.isAttending = isAttending;
            return this;
        }

        public Builder familyGroupId(Long familyGroupId) {
            this.familyGroupId = familyGroupId;
            return this;
        }

        public FamilyMemberResponseDTO build() {
            return new FamilyMemberResponseDTO(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public Boolean getIsAttending() {
        return isAttending;
    }

    public Long getFamilyGroupId() {
        return familyGroupId;
    }

    @Override
    public String toString() {
        return "FamilyMemberResponseDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", dietaryRestrictions='" + dietaryRestrictions + '\'' +
                ", isAttending=" + isAttending +
                ", familyGroupId=" + familyGroupId +
                '}';
    }
}
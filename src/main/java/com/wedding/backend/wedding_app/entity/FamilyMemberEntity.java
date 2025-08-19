package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "family_members")
public class FamilyMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String ageGroup; // 'adult', 'child', 'infant'
    private String dietaryRestrictions;
    private Boolean isAttending;

    @ManyToOne
    @JoinColumn(name = "family_group_id")
    @JsonBackReference
    private FamilyGroupEntity familyGroup;

    public FamilyMemberEntity() {
    }

    public static Builder builder() {
        return new Builder();
    }

    private FamilyMemberEntity(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.ageGroup = builder.ageGroup;
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.isAttending = builder.isAttending;
        this.familyGroup = builder.familyGroup;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String ageGroup;
        private String dietaryRestrictions;
        private Boolean isAttending;
        private FamilyGroupEntity familyGroup;

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

        public Builder familyGroup(FamilyGroupEntity familyGroup) {
            this.familyGroup = familyGroup;
            return this;
        }

        public FamilyMemberEntity build() {
            return new FamilyMemberEntity(this);
        }
    }

    @Override
    public String toString() {
        return """
                FamilyMember:
                    id: %s
                    firstName: %s
                    lastName: %s
                    ageGroup: %s
                    isAttending: %s
                    familyGroupId: %s
                """.formatted(id, firstName, lastName, ageGroup, isAttending,
                familyGroup != null ? familyGroup.getId() : null);
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

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Boolean getIsAttending() {
        return isAttending;
    }

    public void setIsAttending(Boolean isAttending) {
        this.isAttending = isAttending;
    }

    public FamilyGroupEntity getFamilyGroup() {
        return familyGroup;
    }

    public void setFamilyGroup(FamilyGroupEntity familyGroup) {
        this.familyGroup = familyGroup;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FamilyMemberEntity that = (FamilyMemberEntity) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
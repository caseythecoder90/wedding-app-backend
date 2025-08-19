package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "family_groups")
public class FamilyGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    private Integer maxAttendees;
    private OffsetDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "primary_contact_guest_id")
    private GuestEntity primaryContact;

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GuestEntity> guests = new ArrayList<>();

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FamilyMemberEntity> familyMembers = new ArrayList<>();

    public FamilyGroupEntity() {
    }

    public static Builder builder() {
        return new Builder();
    }

    private FamilyGroupEntity(Builder builder) {
        this.groupName = builder.groupName;
        this.maxAttendees = builder.maxAttendees;
        this.createdAt = builder.createdAt;
        this.primaryContact = builder.primaryContact;
        this.guests = builder.guests;
        this.familyMembers = builder.familyMembers;
    }

    public static class Builder {
        private String groupName;
        private Integer maxAttendees;
        private OffsetDateTime createdAt;
        private GuestEntity primaryContact;
        private List<GuestEntity> guests = new ArrayList<>();
        private List<FamilyMemberEntity> familyMembers = new ArrayList<>();

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder maxAttendees(Integer maxAttendees) {
            this.maxAttendees = maxAttendees;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder primaryContact(GuestEntity primaryContact) {
            this.primaryContact = primaryContact;
            return this;
        }

        public Builder guests(List<GuestEntity> guests) {
            this.guests = guests;
            return this;
        }

        public Builder familyMembers(List<FamilyMemberEntity> familyMembers) {
            this.familyMembers = familyMembers;
            return this;
        }

        public FamilyGroupEntity build() {
            return new FamilyGroupEntity(this);
        }
    }

    @Override
    public String toString() {
        return """
                FamilyGroup:
                    id: %s
                    groupName: %s
                    maxAttendees: %s
                    primaryContactId: %s
                """.formatted(id, groupName, maxAttendees, 
                primaryContact != null ? primaryContact.getId() : null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public GuestEntity getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(GuestEntity primaryContact) {
        this.primaryContact = primaryContact;
    }

    public List<GuestEntity> getGuests() {
        return guests;
    }

    public void setGuests(List<GuestEntity> guests) {
        this.guests = guests;
    }

    public List<FamilyMemberEntity> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMemberEntity> familyMembers) {
        this.familyMembers = familyMembers;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FamilyGroupEntity that = (FamilyGroupEntity) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
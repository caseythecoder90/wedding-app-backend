package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "family_groups")
@Data
@ToString(exclude = {"primaryContact", "guests"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private List<GuestEntity> guests = new ArrayList<>();

    @OneToMany(mappedBy = "familyGroup", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<FamilyMemberEntity> familyMembers = new ArrayList<>();
}
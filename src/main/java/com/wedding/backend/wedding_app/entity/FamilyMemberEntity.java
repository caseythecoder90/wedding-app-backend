package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "family_members")
@ToString(exclude = {"familyGroup"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
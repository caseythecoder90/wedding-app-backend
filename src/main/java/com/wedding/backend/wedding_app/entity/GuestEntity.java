package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
@Data
@ToString(exclude = {"familyGroup"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean plusOneAllowed;
    @Builder.Default
    private Boolean isPrimaryContact = false;

    @ManyToOne
    @JoinColumn(name = "family_group_id")
    @JsonBackReference
    private FamilyGroupEntity familyGroup;

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL)
    @JsonManagedReference
    private RSVPEntity rsvp;
    
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<InvitationCodeEntity> invitationCodes = new ArrayList<>();
}

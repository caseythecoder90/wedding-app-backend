package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "rsvps")
@Data
@ToString(exclude = {"guest"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RSVPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "guest_id")
    @JsonBackReference
    private GuestEntity guest;

    private Boolean attending;
    private String dietaryRestrictions;
    private OffsetDateTime submittedAt;
}
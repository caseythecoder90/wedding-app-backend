package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    @JsonBackReference
    private GuestEntity guest;

    private LocalDateTime createdDate;
    private LocalDateTime expiryDate;
    private Boolean used;
    private String codeType; // "PRIMARY", "REPLACEMENT", etc.
}
package com.wedding.backend.wedding_app.entity;


import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "donor_name", nullable = false)
    private String donorName;

    @Column(name = "donor_email")
    private String donorEmail;

    @Column(name = "donor_phone")
    private String donorPhone;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private DonationStatus status = DonationStatus.PENDING;

    @Column(name = "donation_date")
    private OffsetDateTime donationDate;

    @Column(name = "confirmed_date")
    private OffsetDateTime confirmedDate;

    @Column(name = "thank_you_sent_date")
    private OffsetDateTime thankYouSentDate;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // Optional: Link to guest entity if they're on the guest list
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private GuestEntity guest;

}
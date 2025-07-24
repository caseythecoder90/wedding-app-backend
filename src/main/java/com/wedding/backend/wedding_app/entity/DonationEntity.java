package com.wedding.backend.wedding_app.entity;


import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.enums.PaymentMethod;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "donations")
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
    private DonationStatus status = DonationStatus.PENDING;

    @Column(name = "guest_id")
    private Long guestId;

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
    @JoinColumn(name = "guest_id", insertable = false, updatable = false)
    private GuestEntity guest;

    public DonationEntity() {}

    private DonationEntity(Builder builder) {
        this.donorName = builder.donorName;
        this.donorEmail = builder.donorEmail;
        this.donorPhone = builder.donorPhone;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentReference = builder.paymentReference;
        this.message = builder.message;
        this.status = builder.status;
        this.guestId = builder.guestId;
        this.donationDate = builder.donationDate;
        this.confirmedDate = builder.confirmedDate;
        this.thankYouSentDate = builder.thankYouSentDate;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.guest = builder.guest;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String donorName;
        private String donorEmail;
        private String donorPhone;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String paymentReference;
        private String message;
        private DonationStatus status;
        private Long guestId;
        private OffsetDateTime donationDate;
        private OffsetDateTime confirmedDate;
        private OffsetDateTime thankYouSentDate;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private GuestEntity guest;

        public Builder donorName(String donorName) {
            this.donorName = donorName;
            return this;
        }

        public Builder donorEmail(String donorEmail) {
            this.donorEmail = donorEmail;
            return this;
        }

        public Builder donorPhone(String donorPhone) {
            this.donorPhone = donorPhone;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder paymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(DonationStatus status) {
            this.status = status;
            return this;
        }

        public Builder guestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }

        public Builder donationDate(OffsetDateTime donationDate) {
            this.donationDate = donationDate;
            return this;
        }

        public Builder confirmedDate(OffsetDateTime confirmedDate) {
            this.confirmedDate = confirmedDate;
            return this;
        }

        public Builder thankYouSentDate(OffsetDateTime thankYouSentDate) {
            this.thankYouSentDate = thankYouSentDate;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder guest(GuestEntity guest) {
            this.guest = guest;
            return this;
        }

        public DonationEntity build() {
            return new DonationEntity(this);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public OffsetDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(OffsetDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public OffsetDateTime getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(OffsetDateTime confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public OffsetDateTime getThankYouSentDate() {
        return thankYouSentDate;
    }

    public void setThankYouSentDate(OffsetDateTime thankYouSentDate) {
        this.thankYouSentDate = thankYouSentDate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonationEntity that = (DonationEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
                Donation:
                    id: %s
                    donorName: %s
                    amount: %s
                    paymentMethod: %s
                    status: %s
                    donationDate: %s
                """.formatted(id, donorName, amount, paymentMethod, status, donationDate);
    }
}
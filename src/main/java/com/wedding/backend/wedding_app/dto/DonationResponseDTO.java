package com.wedding.backend.wedding_app.dto;

import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class DonationResponseDTO {
    private Long id;
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

    public DonationResponseDTO() {}

    private DonationResponseDTO(Builder builder) {
        this.id = builder.id;
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
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public DonationResponseDTO build() {
            return new DonationResponseDTO(this);
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

    @Override
    public String toString() {
        return """
                DonationResponseDTO:
                    id: %s
                    donorName: %s
                    amount: %s
                    paymentMethod: %s
                    status: %s
                """.formatted(id, donorName, amount, paymentMethod, status);
    }
}

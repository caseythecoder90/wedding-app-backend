package com.wedding.backend.wedding_app.dto;

import com.wedding.backend.wedding_app.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DonationRequestDTO {

    @NotBlank(message = "Donor name is required")
    private String donorName;

    @Email(message = "Valid email is required")
    private String donorEmail;

    private String donorPhone;

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "1.00", message = "Minimum donation is $1.00")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String paymentReference;
    private String message;
    private Long guestId; // Optional: if donor is a wedding guest

    public DonationRequestDTO() {}

    private DonationRequestDTO(Builder builder) {
        this.donorName = builder.donorName;
        this.donorEmail = builder.donorEmail;
        this.donorPhone = builder.donorPhone;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentReference = builder.paymentReference;
        this.message = builder.message;
        this.guestId = builder.guestId;
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
        private Long guestId;

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

        public Builder guestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }

        public DonationRequestDTO build() {
            return new DonationRequestDTO(this);
        }
    }

    // Getters and Setters
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

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return """
                DonationRequestDTO:
                    donorName: %s
                    donorEmail: %s
                    amount: %s
                    paymentMethod: %s
                """.formatted(donorName, donorEmail, amount, paymentMethod);
    }
}

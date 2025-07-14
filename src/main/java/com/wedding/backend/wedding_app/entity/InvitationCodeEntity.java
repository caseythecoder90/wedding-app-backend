package com.wedding.backend.wedding_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_codes")
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

    public InvitationCodeEntity() {}

    private InvitationCodeEntity(Builder builder) {
        this.code = builder.code;
        this.guest = builder.guest;
        this.createdDate = builder.createdDate;
        this.expiryDate = builder.expiryDate;
        this.used = builder.used;
        this.codeType = builder.codeType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private GuestEntity guest;
        private LocalDateTime createdDate;
        private LocalDateTime expiryDate;
        private Boolean used;
        private String codeType;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder guest(GuestEntity guest) {
            this.guest = guest;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder used(Boolean used) {
            this.used = used;
            return this;
        }

        public Builder codeType(String codeType) {
            this.codeType = codeType;
            return this;
        }

        public InvitationCodeEntity build() {
            return new InvitationCodeEntity(this);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        InvitationCodeEntity that = (InvitationCodeEntity) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return """
                InvitationCode:
                    id: %s
                    code: %s
                    guestId: %s
                    createdDate: %s
                    expiryDate: %s
                    used: %s
                    codeType: %s
                """.formatted(
                id, 
                code, 
                guest != null ? guest.getId() : null,
                createdDate,
                expiryDate,
                used,
                codeType);
    }
}
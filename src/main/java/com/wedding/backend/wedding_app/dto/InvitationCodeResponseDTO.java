package com.wedding.backend.wedding_app.dto;

import java.time.LocalDateTime;

public class InvitationCodeResponseDTO {
    private Long id;
    private String code;
    private Long guestId;
    private String guestName;
    private LocalDateTime createdDate;
    private LocalDateTime expiryDate;
    private Boolean used;
    private String codeType;
    
    private InvitationCodeResponseDTO(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.guestId = builder.guestId;
        this.guestName = builder.guestName;
        this.createdDate = builder.createdDate;
        this.expiryDate = builder.expiryDate;
        this.used = builder.used;
        this.codeType = builder.codeType;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String code;
        private Long guestId;
        private String guestName;
        private LocalDateTime createdDate;
        private LocalDateTime expiryDate;
        private Boolean used;
        private String codeType;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder guestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }
        
        public Builder guestName(String guestName) {
            this.guestName = guestName;
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
        
        public InvitationCodeResponseDTO build() {
            return new InvitationCodeResponseDTO(this);
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
    
    public Long getGuestId() {
        return guestId;
    }
    
    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public void setGuestName(String guestName) {
        this.guestName = guestName;
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
        
        InvitationCodeResponseDTO that = (InvitationCodeResponseDTO) o;
        
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
package com.wedding.backend.wedding_app.dto;

import java.time.OffsetDateTime;

public class FamilyGroupResponseDTO {
    private final Long id;
    private final String groupName;
    private final Integer maxAttendees;
    private final Long primaryContactGuestId;
    private final OffsetDateTime createdAt;

    public FamilyGroupResponseDTO(Builder builder) {
        this.id = builder.id;
        this.groupName = builder.groupName;
        this.maxAttendees = builder.maxAttendees;
        this.primaryContactGuestId = builder.primaryContactGuestId;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String groupName;
        private Integer maxAttendees;
        private Long primaryContactGuestId;
        private OffsetDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder maxAttendees(Integer maxAttendees) {
            this.maxAttendees = maxAttendees;
            return this;
        }

        public Builder primaryContactGuestId(Long primaryContactGuestId) {
            this.primaryContactGuestId = primaryContactGuestId;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FamilyGroupResponseDTO build() {
            return new FamilyGroupResponseDTO(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public Long getPrimaryContactGuestId() {
        return primaryContactGuestId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "FamilyGroupResponseDTO{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", maxAttendees=" + maxAttendees +
                ", primaryContactGuestId=" + primaryContactGuestId +
                ", createdAt=" + createdAt +
                '}';
    }
}
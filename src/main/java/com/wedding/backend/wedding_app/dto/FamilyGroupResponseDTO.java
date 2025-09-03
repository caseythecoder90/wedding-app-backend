package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyGroupResponseDTO {
    private Long id;
    private String groupName;
    private Integer maxAttendees;
    private Long primaryContactGuestId;
    private OffsetDateTime createdAt;
}
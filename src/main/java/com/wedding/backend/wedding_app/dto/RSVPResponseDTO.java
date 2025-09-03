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
public class RSVPResponseDTO {
    private Long id;
    private Long guestId;
    private String guestName;
    private String guestEmail;
    private Boolean attending;
    private String dietaryRestrictions;
    private OffsetDateTime submittedAt;
}
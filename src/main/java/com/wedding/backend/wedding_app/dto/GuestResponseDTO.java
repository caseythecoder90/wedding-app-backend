package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean plusOneAllowed;
    private Boolean hasRsvp;
    private Long rsvpId;
}
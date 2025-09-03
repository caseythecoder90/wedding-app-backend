package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMemberResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String ageGroup;
    private String dietaryRestrictions;
    private Boolean isAttending;
    private Long familyGroupId;
}
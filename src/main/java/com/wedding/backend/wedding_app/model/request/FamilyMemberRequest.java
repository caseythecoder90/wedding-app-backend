package com.wedding.backend.wedding_app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberRequest {
    private String firstName;
    private String lastName;
    private String ageGroup;
    private String dietaryRestrictions;
    private Boolean isAttending;
}
package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RSVPRequestDTO {
    private Long guestId;
    private Boolean attending;
    private String dietaryRestrictions;
    private String email;
    private boolean sendConfirmationEmail;
    private String preferredLanguage; // "en" or "pt-BR"
    private OffsetDateTime submittedAt;
    
    // Family members - includes existing family members and new additional guests up to maxAttendees limit
    private List<FamilyMemberRSVPRequest> familyMembers;
    
    // Nested class for family member RSVP data (handles both existing and new family members)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FamilyMemberRSVPRequest {
        private Long familyMemberId; // null for new family members/additional guests
        private String firstName;
        private String lastName;
        private String ageGroup;
        private Boolean isAttending;
        private String dietaryRestrictions;
    }
}
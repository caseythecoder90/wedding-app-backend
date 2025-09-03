package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationValidationResponseDTO {
    
    // Primary guest info (the person who got the invitation code)
    private GuestResponseDTO primaryGuest;
    
    // RSVP info
    private RSVPResponseDTO existingRsvp;
    private Boolean hasExistingRsvp;
    
    // Guest type information
    private GuestType guestType;
    
    // Family-related fields (only populated for family invitations)
    private FamilyGroupResponseDTO familyGroup;
    private List<FamilyMemberResponseDTO> familyMembers;
    
    // Plus-one information (for solo guests)
    private Boolean canBringPlusOne;
    
    public enum GuestType {
        SOLO,              // Single guest, no plus-one
        SOLO_WITH_PLUS_ONE, // Single guest with plus-one allowed
        FAMILY_PRIMARY     // Primary contact for a family group
    }
}
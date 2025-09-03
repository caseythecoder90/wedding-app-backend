package com.wedding.backend.wedding_app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupRequest {
    private String groupName;
    private Integer maxAttendees;
    private GuestRequest primaryContact;
    private List<FamilyMemberRequest> familyMembers;
}
package com.wedding.backend.wedding_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for RSVP summary information
 * Used to transfer data from RSVPService to EmailService without creating circular dependencies
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSVPSummaryDTO {
    private int totalRsvps;
    private long totalAttending;
    private long totalNotAttending;
    private long totalGuests;
    private List<RSVPResponseDTO> attendingRsvps;
    private List<RSVPResponseDTO> notAttendingRsvps;
    private String lastUpdated;
    private List<Map<String, Object>> attendingFamilyMembers;
}
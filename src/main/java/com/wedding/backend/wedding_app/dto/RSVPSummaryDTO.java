package com.wedding.backend.wedding_app.dto;

import java.util.List;

/**
 * Data Transfer Object for RSVP summary information
 * Used to transfer data from RSVPService to EmailService without creating circular dependencies
 */
public class RSVPSummaryDTO {
    private int totalRsvps;
    private long totalAttending;
    private long totalNotAttending;
    private long totalGuests;
    private List<RSVPResponseDTO> attendingRsvps;
    private List<RSVPResponseDTO> notAttendingRsvps;
    private String lastUpdated;
    
    // Default constructor
    public RSVPSummaryDTO() {
    }
    
    // Getters and Setters
    public int getTotalRsvps() {
        return totalRsvps;
    }
    
    public void setTotalRsvps(int totalRsvps) {
        this.totalRsvps = totalRsvps;
    }
    
    public long getTotalAttending() {
        return totalAttending;
    }
    
    public void setTotalAttending(long totalAttending) {
        this.totalAttending = totalAttending;
    }
    
    public long getTotalNotAttending() {
        return totalNotAttending;
    }
    
    public void setTotalNotAttending(long totalNotAttending) {
        this.totalNotAttending = totalNotAttending;
    }
    
    public long getTotalGuests() {
        return totalGuests;
    }
    
    public void setTotalGuests(long totalGuests) {
        this.totalGuests = totalGuests;
    }
    
    public List<RSVPResponseDTO> getAttendingRsvps() {
        return attendingRsvps;
    }
    
    public void setAttendingRsvps(List<RSVPResponseDTO> attendingRsvps) {
        this.attendingRsvps = attendingRsvps;
    }
    
    public List<RSVPResponseDTO> getNotAttendingRsvps() {
        return notAttendingRsvps;
    }
    
    public void setNotAttendingRsvps(List<RSVPResponseDTO> notAttendingRsvps) {
        this.notAttendingRsvps = notAttendingRsvps;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private final RSVPSummaryDTO instance = new RSVPSummaryDTO();
        
        public Builder totalRsvps(int totalRsvps) {
            instance.setTotalRsvps(totalRsvps);
            return this;
        }
        
        public Builder totalAttending(long totalAttending) {
            instance.setTotalAttending(totalAttending);
            return this;
        }
        
        public Builder totalNotAttending(long totalNotAttending) {
            instance.setTotalNotAttending(totalNotAttending);
            return this;
        }
        
        public Builder totalGuests(long totalGuests) {
            instance.setTotalGuests(totalGuests);
            return this;
        }
        
        public Builder attendingRsvps(List<RSVPResponseDTO> attendingRsvps) {
            instance.setAttendingRsvps(attendingRsvps);
            return this;
        }
        
        public Builder notAttendingRsvps(List<RSVPResponseDTO> notAttendingRsvps) {
            instance.setNotAttendingRsvps(notAttendingRsvps);
            return this;
        }
        
        public Builder lastUpdated(String lastUpdated) {
            instance.setLastUpdated(lastUpdated);
            return this;
        }
        
        public RSVPSummaryDTO build() {
            return instance;
        }
    }
}
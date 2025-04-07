package com.wedding.backend.wedding_app.model.exception;

public class Detail {
    private String field;
    private String reason;
    
    // Constructor
    public Detail(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }
    
    // Factory method
    public static Detail create(String field, String reason) {
        return new Detail(field, reason);
    }
    
    // Getters and Setters
    public String getField() {
        return field;
    }
    
    public void setField(String field) {
        this.field = field;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Detail detail = (Detail) o;
        
        if (field != null ? !field.equals(detail.field) : detail.field != null) return false;
        return reason != null ? reason.equals(detail.reason) : detail.reason == null;
    }
    
    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Detail{" +
                "field='" + field + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}

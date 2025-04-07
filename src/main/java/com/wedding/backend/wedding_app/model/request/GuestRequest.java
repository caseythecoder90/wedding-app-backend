package com.wedding.backend.wedding_app.model.request;

public class GuestRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean plusOneAllowed;
    
    // Default constructor
    public GuestRequest() {
    }
    
    // Constructor with all fields
    private GuestRequest(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.plusOneAllowed = builder.plusOneAllowed;
    }
    
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isPlusOneAllowed() {
        return plusOneAllowed;
    }
    
    public void setPlusOneAllowed(boolean plusOneAllowed) {
        this.plusOneAllowed = plusOneAllowed;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private boolean plusOneAllowed;
        
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder plusOneAllowed(boolean plusOneAllowed) {
            this.plusOneAllowed = plusOneAllowed;
            return this;
        }
        
        public GuestRequest build() {
            return new GuestRequest(this);
        }
    }
    
    @Override
    public String toString() {
        return "GuestRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", plusOneAllowed=" + plusOneAllowed +
                '}';
    }
}
# Wedding App API Test Plan

This document outlines the comprehensive testing strategy for the wedding application API to ensure all functionality works correctly before frontend development.

## 1. Guest Management

### 1.1 Create Test Data
- Create 10-15 fictional guests using Postman Collection Runner with varying attributes:
  - Some with plusOneAllowed=true, others with false
  - Varying email addresses (some blank for testing email validation)
  - Different name combinations
- Verify all guests are persisted correctly in the database

### 1.2 Guest Retrieval
- Test retrieving all guests
- Test retrieving individual guests by ID
- Test guest not found scenarios
- Verify response format matches expected DTO structure

## 2. Invitation Code Generation

### 2.1 Generate Invitation Codes
- Generate invitation codes for all guests
- Verify format follows expected pattern (WED + 6 alphanumeric characters)
- Verify each code is unique
- Test retrieving existing invitation codes

### 2.2 QR Code Generation
- Generate QR codes for all invitation codes
- Verify QR images are created successfully with correct dimensions (300x300)
- Test decoding QR codes to ensure they contain correct invitation URLs
- Verify QR codes work when scanned with multiple QR code readers
- Test response headers to ensure proper content type (image/png)
- Test QR code regeneration for the same invitation code

## 3. Invitation Validation

### 3.1 Valid Invitation Tests
- Test validating each invitation code
- Verify guest details returned for valid codes
- Test same code multiple times to ensure consistency
- Verify response includes all required guest information

### 3.2 Invalid Invitation Tests
- Test validation with nonexistent codes
- Test with malformed codes (wrong format)
- Test with expired codes (if applicable)
- Verify appropriate error responses and status codes

## 4. RSVP Flow

### 4.1 Initial RSVP Submission
- Submit RSVPs for half of guests (attending with various combinations):
  - Some attending with plus-ones
  - Some attending without plus-ones
  - Some not attending
  - Some with dietary restrictions
- Verify HTTP status codes (201 Created for new RSVPs)
- Verify response data matches submission

### 4.2 RSVP Retrieval
- Retrieve RSVPs by guest ID for each submission
- Verify all RSVP data matches submission
- Test retrieving all RSVPs
- Verify response formatting and completeness

### 4.3 RSVP Updates
- Update existing RSVPs with changed information:
  - Change attending status
  - Add/remove plus-one
  - Update dietary restrictions
  - Add email where previously blank
- Verify HTTP status codes (200 OK for updates)
- Verify data is correctly persisted

### 4.4 RSVP Restrictions
- Test plus-one restriction enforcement:
  - Try adding plus-one for guest where plusOneAllowed=false
  - Verify appropriate error response
- Test other validation rules like required fields

## 5. Email Notifications

### 5.1 Email Configuration
- Verify email configuration is properly loaded from application.yml
- Test with valid and invalid SMTP settings
- Verify fallback to defaults when configuration is missing

### 5.2 Guest Confirmation Emails
- Test confirmation emails for attending guests:
  - Verify email content follows attending template
  - Check personalization (guest name, plus-one info)
  - Verify correct subject line
- Test confirmation emails for non-attending guests:
  - Verify email content follows non-attending template
  - Check personalization
  - Verify correct subject line
- Test with and without sendConfirmationEmail flag
- Verify emails render correctly in different email clients
- Test email delivery timing and async behavior

### 5.3 Admin Notifications
- Test admin notification emails:
  - Verify summary statistics are correct
  - Verify attending/not-attending lists are accurate
  - Check that triggered guest information is prominently displayed
- Test with admin email enabled/disabled in configuration
- Test summary generation with large dataset

## 6. Error Handling

### 6.1 Validation Errors
- Test all required fields with missing data
- Test with invalid data formats
- Verify appropriate error responses (400 Bad Request)
- Check error message clarity and helpfulness

### 6.2 Not Found Scenarios
- Test operations on nonexistent guests/RSVPs
- Verify appropriate 404 responses
- Check error message format and content

### 6.3 Server Errors
- Test database connection issues (if possible)
- Test email service failures
- Verify error logging is comprehensive
- Check that error responses don't expose sensitive information

## 7. Performance Tests

### 7.1 Load Testing
- Test concurrent RSVP submissions
- Test multiple invitation code validations
- Verify system handles simultaneous requests
- Measure response times under load

### 7.2 Database Performance
- Test query performance with larger datasets
- Verify indices are properly utilized

## 8. Security Tests

### 8.1 Input Validation
- Test with malicious inputs (SQL injection, XSS)
- Test with extremely large payloads
- Verify input sanitization

### 8.2 Authentication (if applicable)
- Test any admin endpoints with/without authentication
- Verify proper authorization controls

## Execution Strategy

1. Create a Postman Collection for automated testing:
   - Organize into folders by feature area
   - Use environment variables for base URL, test accounts
   - Set up pre-request scripts for test data generation

2. Test Data Management:
   - Create scripts to generate test guests with varied attributes
   - Include setup and teardown procedures
   - Document test data for traceability

3. Test Execution:
   - Run tests in logical order (create guests before testing RSVPs)
   - Execute complete test suite after any significant changes
   - Save test results for comparison when fixing bugs

4. Documentation:
   - Document any deviations from expected behavior
   - Track bugs and fixes
   - Update test cases as requirements evolve

5. Continuous Integration:
   - Consider integrating tests with CI pipeline if available
   - Run basic test suite automatically on commits

## Special Test Areas

### QR Code Verification Process
1. Generate QR codes for 5 different invitation codes
2. Save QR images to local storage
3. Use multiple QR scanner applications to verify each code
4. Confirm each code resolves to the correct URL with proper invitation code
5. Test in different lighting conditions and from different distances/angles
6. Verify QR code appearance (contrast, clarity, error correction)

### Email Delivery and Rendering Tests
1. Set up test email accounts on different providers (Gmail, Outlook, etc.)
2. Send confirmation emails to each test account
3. Verify delivery time
4. Check rendering in different mail clients (desktop, mobile, web)
5. Verify all links are working
6. Test with valid and invalid email addresses
7. Verify HTML and plain text alternatives
8. Check for SMTP errors and fallback behavior

This test plan should be revisited and updated as development progresses and any additional features are added to the application.
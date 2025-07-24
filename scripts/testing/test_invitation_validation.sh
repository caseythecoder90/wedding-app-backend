#!/bin/bash

# Test invitation code validation script
# Make sure your backend is running locally before executing

BASE_URL="http://localhost:8080"  # Adjust port if different
VALIDATE_ENDPOINT="/v1/api/invitation/validate"
GET_CODES_ENDPOINT="/v1/api/invitation/codes/guest"

echo "Testing Invitation Code Validation"
echo "=================================="

# Function to test validation
test_validation() {
    local code=$1
    local description=$2
    
    echo "Testing: $description"
    echo "Code: $code"
    
    response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
        -X GET "${BASE_URL}${VALIDATE_ENDPOINT}/${code}")
    
    http_status=$(echo "$response" | grep "HTTP_STATUS" | cut -d: -f2)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_status" -eq 200 ]; then
        echo "✓ SUCCESS: Valid invitation code"
        echo "Response: $body" | jq '.' 2>/dev/null || echo "Response: $body"
    else
        echo "✗ FAILED: Invalid invitation code (HTTP $http_status)"
        echo "Response: $body"
    fi
    echo "----------------------------------------"
}

# Get some valid codes first
echo "Fetching invitation codes for testing..."
read -p "Enter a guest ID to get their invitation codes (or press Enter to skip): " guest_id

if [ ! -z "$guest_id" ]; then
    echo "Getting codes for guest ID: $guest_id"
    response=$(curl -s "${BASE_URL}${GET_CODES_ENDPOINT}/${guest_id}")
    
    if command -v jq >/dev/null 2>&1; then
        echo "Available codes:"
        echo "$response" | jq -r '.[] | "Code: \(.code) (Type: \(.codeType))"' 2>/dev/null || echo "No codes found or invalid response"
        
        # Extract first code for testing
        valid_code=$(echo "$response" | jq -r '.[0].code' 2>/dev/null)
        if [ "$valid_code" != "null" ] && [ ! -z "$valid_code" ]; then
            echo
            echo "Running validation tests..."
            echo
            
            # Test 1: Valid code
            test_validation "$valid_code" "Valid invitation code from guest $guest_id"
        fi
    else
        echo "Response: $response"
        echo "Install jq for better JSON parsing"
    fi
fi

# Test 2: Invalid code
test_validation "WED-INVALID123" "Invalid invitation code"

# Test 3: Malformed code
test_validation "INVALID" "Malformed invitation code"

# Test 4: Empty code
test_validation "" "Empty invitation code"

echo "Invitation validation testing completed!"
#!/bin/bash

# Test RSVP submission script
# Make sure your backend is running locally before executing

BASE_URL="http://localhost:8080"  # Adjust port if different
RSVP_ENDPOINT="/v1/api/rsvps"

echo "Testing RSVP Submission"
echo "======================"

# Function to submit RSVP
submit_rsvp() {
    local guest_id=$1
    local attending=$2
    local plus_one=$3
    local plus_one_name=$4
    local dietary=$5
    local email=$6
    local description=$7
    
    echo "Testing: $description"
    echo "Guest ID: $guest_id, Attending: $attending, Plus One: $plus_one"
    
    # Build JSON payload
    json_payload=$(cat <<EOF
{
    "guestId": $guest_id,
    "attending": $attending,
    "bringingPlusOne": $plus_one,
    "plusOneName": $(if [ "$plus_one_name" != "null" ]; then echo "\"$plus_one_name\""; else echo "null"; fi),
    "dietaryRestrictions": $(if [ "$dietary" != "null" ]; then echo "\"$dietary\""; else echo "null"; fi),
    "email": "$(if [ "$email" != "null" ]; then echo "$email"; else echo "test@example.com"; fi)",
    "sendConfirmationEmail": false,
    "submittedAt": "$(date -u +"%Y-%m-%dT%H:%M:%S.%3NZ")"
}
EOF
)
    
    response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
        -X POST "${BASE_URL}${RSVP_ENDPOINT}" \
        -H "Content-Type: application/json" \
        -d "$json_payload")
    
    http_status=$(echo "$response" | grep "HTTP_STATUS" | cut -d: -f2)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_status" -eq 200 ] || [ "$http_status" -eq 201 ]; then
        echo "✓ SUCCESS: RSVP submitted (HTTP $http_status)"
        echo "Response: $body" | jq '.' 2>/dev/null || echo "Response: $body"
    else
        echo "✗ FAILED: RSVP submission failed (HTTP $http_status)"
        echo "Response: $body"
    fi
    echo "----------------------------------------"
}

# Get guest ID for testing
read -p "Enter a guest ID to test RSVP submission: " guest_id

if [ -z "$guest_id" ]; then
    echo "No guest ID provided. Exiting."
    exit 1
fi

if ! [[ "$guest_id" =~ ^[0-9]+$ ]]; then
    echo "Error: Please enter a valid numeric guest ID"
    exit 1
fi

echo
echo "Running RSVP submission tests for guest ID: $guest_id..."
echo

# Test 1: Attending with plus one
submit_rsvp "$guest_id" "true" "true" "John Doe" "Vegetarian" "test@example.com" "Attending with plus one and dietary restrictions"

# Test 2: Attending without plus one
submit_rsvp "$guest_id" "true" "false" "null" "No restrictions" "test@example.com" "Attending without plus one"

# Test 3: Not attending
submit_rsvp "$guest_id" "false" "false" "null" "null" "test@example.com" "Not attending"

# Test 4: Update previous RSVP
submit_rsvp "$guest_id" "true" "true" "Jane Smith" "Gluten-free" "updated@example.com" "Updating previous RSVP"

echo "RSVP submission testing completed!"

# Show final RSVP status
echo
echo "Final RSVP status for guest $guest_id:"
final_response=$(curl -s "${BASE_URL}${RSVP_ENDPOINT}/${guest_id}")
echo "$final_response" | jq '.' 2>/dev/null || echo "$final_response"
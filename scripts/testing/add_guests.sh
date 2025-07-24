#!/bin/bash

# Add guests script
# Make sure your backend is running locally before executing

BASE_URL="http://localhost:8080"  # Adjust port if different
ADD_ENDPOINT="/v1/api/guests/create"     # Adjust endpoint path if different
GUESTS_DATA_FILE="../../src/main/resources/mockdata/guests-data.json"

echo "Starting guest creation process..."

# Check if guests data file exists
if [ ! -f "$GUESTS_DATA_FILE" ]; then
    echo "Error: guests-data.json file not found at $GUESTS_DATA_FILE"
    exit 1
fi

# Read the JSON file and parse each guest
jq -c '.[]' "$GUESTS_DATA_FILE" | while read guest; do
    firstName=$(echo "$guest" | jq -r '.firstName')
    lastName=$(echo "$guest" | jq -r '.lastName')
    
    echo "Adding guest: $firstName $lastName"
    
    response=$(curl -s -o /dev/null -w "%{http_code}" \
        -X POST "${BASE_URL}${ADD_ENDPOINT}" \
        -H "Content-Type: application/json" \
        -d "$guest")
    
    if [ "$response" -eq 200 ] || [ "$response" -eq 201 ]; then
        echo "✓ Successfully added guest: $firstName $lastName"
    else
        echo "✗ Failed to add guest: $firstName $lastName (HTTP $response)"
    fi
    
    # Small delay to avoid overwhelming the server
    sleep 0.1
done

echo "Guest creation process completed!"
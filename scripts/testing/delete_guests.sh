#!/bin/bash

# Delete all guests script
# Make sure your backend is running locally before executing

BASE_URL="http://localhost:8080"  # Adjust port if different
DELETE_ENDPOINT="/v1/api/guests"     # Adjust endpoint path if different

# Guest IDs from your JSON data
GUEST_IDS=(3 4 6 7 8 9 10 11 12 13 15 16 17 18 19 20 21 22 23 24 25 14 26)

echo "Starting guest deletion process..."
echo "Total guests to delete: ${#GUEST_IDS[@]}"

for id in "${GUEST_IDS[@]}"; do
    echo "Deleting guest ID: $id"
    response=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "${BASE_URL}${DELETE_ENDPOINT}/${id}")
    
    if [ "$response" -eq 200 ] || [ "$response" -eq 204 ]; then
        echo "✓ Successfully deleted guest ID: $id"
    else
        echo "✗ Failed to delete guest ID: $id (HTTP $response)"
    fi
done

echo "Guest deletion process completed!"
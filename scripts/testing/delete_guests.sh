#!/bin/bash

# Delete all guests script

BASE_URL="http://localhost:8080"
DELETE_ENDPOINT="/v1/api/guests"

# Guest IDs
GUEST_IDS=(30)

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
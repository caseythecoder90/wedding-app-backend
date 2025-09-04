#!/bin/bash

# Generate invitation codes script
# Make sure your backend is running locally before executing

BASE_URL="https://wedding-app-backend-production.up.railway.app"  # Adjust port if different
GENERATE_ENDPOINT="/v1/api/invitation/code/generate"  # Adjust endpoint path if different

echo "Generate Invitation Codes for Guest Range"
echo "============================================"

# Prompt for guest ID range
read -p "Enter starting guest ID: " start_id
read -p "Enter ending guest ID: " end_id

# Validate input
if ! [[ "$start_id" =~ ^[0-9]+$ ]] || ! [[ "$end_id" =~ ^[0-9]+$ ]]; then
    echo "Error: Please enter valid numeric IDs"
    exit 1
fi

if [ "$start_id" -gt "$end_id" ]; then
    echo "Error: Starting ID must be less than or equal to ending ID"
    exit 1
fi

echo "Generating invitation codes for guests $start_id to $end_id..."
echo

total_guests=$((end_id - start_id + 1))
success_count=0
failed_count=0

for ((id=start_id; id<=end_id; id++)); do
    echo "Generating invitation code for guest ID: $id"
    
    response=$(curl -s -o /dev/null -w "%{http_code}" \
        -X POST "${BASE_URL}${GENERATE_ENDPOINT}/${id}")
    
    if [ "$response" -eq 200 ] || [ "$response" -eq 201 ]; then
        echo "✓ Successfully generated invitation code for guest ID: $id"
        ((success_count++))
    else
        echo "✗ Failed to generate invitation code for guest ID: $id (HTTP $response)"
        ((failed_count++))
    fi
    
    # Small delay to avoid overwhelming the server
    sleep 0.1
done

echo
echo "Invitation code generation completed!"
echo "Total guests processed: $total_guests"
echo "Successful: $success_count"
echo "Failed: $failed_count"
#!/bin/bash

# Wedding Guest Setup Script
# Reads guest-setup-template.json and creates family groups via API

API_BASE_URL="http://localhost:8080/v1/api"
GUEST_DATA_FILE="./guest-setup-template.json"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}🎪 Wedding Guest Setup Script${NC}"
echo -e "${BLUE}=============================${NC}\n"

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo -e "${RED}❌ Error: jq is required but not installed.${NC}"
    echo "Please install jq: brew install jq"
    exit 1
fi

# Check if the data file exists
if [ ! -f "$GUEST_DATA_FILE" ]; then
    echo -e "${RED}❌ Error: Guest data file not found: $GUEST_DATA_FILE${NC}"
    exit 1
fi

# Check API health
echo -e "${YELLOW}🏥 Checking API health...${NC}"
if curl -s -f "${API_BASE_URL%/v1/api}/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ API is healthy${NC}\n"
else
    echo -e "${YELLOW}⚠️  Could not reach API health endpoint, continuing anyway...${NC}\n"
fi

echo -e "${BLUE}🚀 Starting guest setup...${NC}\n"

# Counters
SUCCESS_COUNT=0
ERROR_COUNT=0
TOTAL_GROUPS=$(jq '.familyGroups | length' "$GUEST_DATA_FILE")

# Process each family group
for i in $(seq 0 $((TOTAL_GROUPS - 1))); do
    # Extract group data
    GROUP_NAME=$(jq -r ".familyGroups[$i].groupName // \"Unknown\"" "$GUEST_DATA_FILE")
    GROUP_TYPE=$(jq -r ".familyGroups[$i].type" "$GUEST_DATA_FILE")
    
    echo -e "${BLUE}📝 Processing $((i + 1))/$TOTAL_GROUPS: $GROUP_NAME${NC}"
    
    # Skip SOLO types
    if [ "$GROUP_TYPE" = "SOLO" ]; then
        echo -e "   ${YELLOW}⏭️  Skipping SOLO guest - will create individual guest later${NC}\n"
        continue
    fi
    
    # Create the API request JSON
    API_REQUEST=$(jq -c "
    .familyGroups[$i] | {
        groupName: .groupName,
        maxAttendees: (.maxAttendees | if type == \"string\" then tonumber else . end),
        primaryContact: {
            firstName: .primaryContact.firstName,
            lastName: .primaryContact.lastName,
            email: (.primaryContact.email // \"\"),
            phone: (.primaryContact.phone // \"\"),
            plusOneAllowed: .primaryContact.plusOneAllowed
        },
        familyMembers: [
            .familyMembers[] | {
                firstName: .firstName,
                lastName: .lastName,
                ageGroup: (.ageGroup // \"adult\"),
                dietaryRestrictions: (.dietaryRestrictions // \"\"),
                isAttending: (.isAttending // false)
            }
        ]
    }" "$GUEST_DATA_FILE")
    
    # Make the API call
    RESPONSE=$(curl -s -w "\n%{http_code}" \
        -X POST \
        -H "Content-Type: application/json" \
        -d "$API_REQUEST" \
        "$API_BASE_URL/family-groups")
    
    # Extract response body and status code
    RESPONSE_BODY=$(echo "$RESPONSE" | head -n -1)
    HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
    
    if [ "$HTTP_CODE" -eq 201 ] || [ "$HTTP_CODE" -eq 200 ]; then
        FAMILY_GROUP_ID=$(echo "$RESPONSE_BODY" | jq -r '.id // "unknown"')
        PRIMARY_CONTACT_ID=$(echo "$RESPONSE_BODY" | jq -r '.primaryContactGuestId // "unknown"')
        MAX_ATTENDEES=$(echo "$RESPONSE_BODY" | jq -r '.maxAttendees // "unknown"')
        
        echo -e "   ${GREEN}✅ Created family group ID: $FAMILY_GROUP_ID${NC}"
        echo -e "   ${GREEN}👤 Primary contact: $PRIMARY_CONTACT_ID${NC}"
        echo -e "   ${GREEN}👥 Max attendees: $MAX_ATTENDEES${NC}\n"
        
        ((SUCCESS_COUNT++))
    else
        echo -e "   ${RED}❌ Error: HTTP $HTTP_CODE${NC}"
        echo -e "   ${RED}Response: $RESPONSE_BODY${NC}\n"
        ((ERROR_COUNT++))
    fi
    
    # Small delay to avoid overwhelming the server
    sleep 0.1
done

# Summary
echo -e "${BLUE}📊 Setup Summary:${NC}"
echo -e "${GREEN}✅ Successfully created: $SUCCESS_COUNT family groups${NC}"
echo -e "${RED}❌ Errors: $ERROR_COUNT${NC}"

if [ $ERROR_COUNT -eq 0 ]; then
    echo -e "\n${GREEN}🎉 All guests setup completed successfully!${NC}"
else
    echo -e "\n${YELLOW}⚠️  Setup completed with some errors. Check the output above.${NC}"
fi
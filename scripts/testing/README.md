# Testing Scripts

This directory contains scripts for testing and development purposes.

## Scripts

### delete_guests.sh
Deletes all guests from the database via API calls. Useful for cleaning up test data.

**Prerequisites:**
- Backend server running locally
- Update `BASE_URL` and `DELETE_ENDPOINT` variables if needed

**Usage:**
```bash
cd scripts/testing
./delete_guests.sh
```

**Note:** This will cascade delete all associated RSVPs and invitation codes due to the `CascadeType.ALL` configuration in the entity relationships.

### add_guests.sh
Adds guests to the database using the mock data from `guests-data.json`.

**Prerequisites:**
- Backend server running locally
- `jq` command-line JSON processor installed
- Update `BASE_URL` and `ADD_ENDPOINT` variables if needed

**Usage:**
```bash
cd scripts/testing
./add_guests.sh
```

**Note:** The script reads from `../../src/main/resources/mockdata/guests-data.json` and creates each guest via API calls.

### generate_invitation_codes.sh
Generates invitation codes for a range of guest IDs.

**Prerequisites:**
- Backend server running locally
- Guests must already exist in the database
- Update `BASE_URL` and `GENERATE_ENDPOINT` variables if needed

**Usage:**
```bash
cd scripts/testing
./generate_invitation_codes.sh
```

**Note:** The script prompts for start and end guest IDs, then generates invitation codes for all guests in that range.

### test_invitation_validation.sh
Tests invitation code validation with both valid and invalid codes.

**Prerequisites:**
- Backend server running locally
- Some invitation codes already generated
- `jq` for JSON parsing (optional but recommended)

**Usage:**
```bash
cd scripts/testing
./test_invitation_validation.sh
```

**Note:** The script can fetch existing invitation codes for a guest ID, then test validation with valid and invalid codes.

### test_rsvp_submission.sh
Tests RSVP submission and updates for a guest.

**Prerequisites:**
- Backend server running locally
- Valid guest ID in the database
- `jq` for JSON parsing (optional but recommended)

**Usage:**
```bash
cd scripts/testing
./test_rsvp_submission.sh
```

**Note:** The script tests multiple RSVP scenarios: attending with/without plus one, not attending, and updating existing RSVPs.
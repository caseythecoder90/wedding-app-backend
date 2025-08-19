-- Migration script: Add family groups and family members support
-- This script adds the necessary tables and columns to support family-based RSVPs

-- Create family_groups table
CREATE TABLE IF NOT EXISTS family_groups (
    id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(255),
    max_attendees INTEGER NOT NULL DEFAULT 1,
    primary_contact_guest_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create family_members table
CREATE TABLE IF NOT EXISTS family_members (
    id BIGSERIAL PRIMARY KEY,
    family_group_id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    age_group VARCHAR(50), -- 'adult', 'child', 'infant'
    dietary_restrictions TEXT,
    is_attending BOOLEAN,
    CONSTRAINT fk_family_members_family_group 
        FOREIGN KEY (family_group_id) 
        REFERENCES family_groups(id) 
        ON DELETE CASCADE
);

-- Add family-related columns to existing guests table
ALTER TABLE guests 
ADD COLUMN IF NOT EXISTS family_group_id BIGINT,
ADD COLUMN IF NOT EXISTS is_primary_contact BOOLEAN DEFAULT false;

-- Add foreign key constraints
ALTER TABLE guests 
ADD CONSTRAINT IF NOT EXISTS fk_guests_family_group 
    FOREIGN KEY (family_group_id) 
    REFERENCES family_groups(id) 
    ON DELETE SET NULL;

ALTER TABLE family_groups 
ADD CONSTRAINT IF NOT EXISTS fk_family_groups_primary_contact 
    FOREIGN KEY (primary_contact_guest_id) 
    REFERENCES guests(id) 
    ON DELETE SET NULL;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_guests_family_group_id ON guests(family_group_id);
CREATE INDEX IF NOT EXISTS idx_family_members_family_group_id ON family_members(family_group_id);
CREATE INDEX IF NOT EXISTS idx_guests_is_primary_contact ON guests(is_primary_contact);

-- Comments for documentation
COMMENT ON TABLE family_groups IS 'Groups that represent families or multiple guests sharing an invitation';
COMMENT ON COLUMN family_groups.group_name IS 'Display name for the family group (e.g., "Smith Family")';
COMMENT ON COLUMN family_groups.max_attendees IS 'Maximum number of people allowed in this family group';
COMMENT ON COLUMN family_groups.primary_contact_guest_id IS 'The main guest who receives invitation codes and RSVPs for the family';

COMMENT ON TABLE family_members IS 'Additional family members who are not full guests but are part of a family RSVP';
COMMENT ON COLUMN family_members.age_group IS 'Age category: adult, child, or infant - used for meal planning';
COMMENT ON COLUMN family_members.is_attending IS 'Whether this family member is attending (from RSVP response)';

COMMENT ON COLUMN guests.family_group_id IS 'Reference to family group if this guest is part of a family';
COMMENT ON COLUMN guests.is_primary_contact IS 'Whether this guest is the primary contact for their family group';
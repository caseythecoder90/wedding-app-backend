package com.wedding.backend.wedding_app.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FamilyDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(FamilyDatabaseService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeFamilyDatabase() {
        log.info("BEGIN - Initializing family database schema and migrations");

        try {
            addFamilyColumns();
            createFamilyIndexes();
            createFamilyConstraints();
            migrateExistingGuestsToFamilySystem();
            log.info("END - Family database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during family database initialization", e);
        }
    }

    private void addFamilyColumns() {
        log.info("Adding family-related columns to existing tables...");

        // Add family columns to guests table if they don't exist
        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM information_schema.columns 
                    WHERE table_name='guests' AND column_name='family_group_id'
                ) THEN
                    ALTER TABLE guests ADD COLUMN family_group_id BIGINT;
                END IF;
            END $$
            """);

        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM information_schema.columns 
                    WHERE table_name='guests' AND column_name='is_primary_contact'
                ) THEN
                    ALTER TABLE guests ADD COLUMN is_primary_contact BOOLEAN DEFAULT false;
                END IF;
            END $$
            """);
    }

    private void createFamilyIndexes() {
        log.info("Creating family database indexes...");

        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guests_family_group_id ON guests(family_group_id)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_family_members_family_group_id ON family_members(family_group_id)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guests_is_primary_contact ON guests(is_primary_contact)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_family_groups_primary_contact ON family_groups(primary_contact_guest_id)");
    }

    private void createFamilyConstraints() {
        log.info("Creating family database constraints...");

        // Add foreign key from guests to family_groups
        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint 
                    WHERE conname = 'fk_guests_family_group'
                ) THEN
                    ALTER TABLE guests 
                    ADD CONSTRAINT fk_guests_family_group 
                    FOREIGN KEY (family_group_id) 
                    REFERENCES family_groups(id) 
                    ON DELETE SET NULL;
                END IF;
            END $$
            """);

        // Add foreign key from family_groups to guests (primary contact)
        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint 
                    WHERE conname = 'fk_family_groups_primary_contact'
                ) THEN
                    ALTER TABLE family_groups 
                    ADD CONSTRAINT fk_family_groups_primary_contact 
                    FOREIGN KEY (primary_contact_guest_id) 
                    REFERENCES guests(id) 
                    ON DELETE SET NULL;
                END IF;
            END $$
            """);

        // Add check constraint for positive max_attendees
        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint 
                    WHERE conname = 'check_positive_max_attendees'
                ) THEN
                    ALTER TABLE family_groups ADD CONSTRAINT check_positive_max_attendees CHECK (max_attendees > 0);
                END IF;
            END $$
            """);
    }

    private void migrateExistingGuestsToFamilySystem() {
        log.info("Migrating existing guests to family system...");

        // Create individual family groups for existing guests who don't have one
        String createFamilyGroupsSQL = """
            INSERT INTO family_groups (group_name, max_attendees, primary_contact_guest_id, created_at)
            SELECT 
                CONCAT(g.first_name, ' ', g.last_name, ' Group') as group_name,
                CASE 
                    WHEN g.plus_one_allowed = true THEN 2 
                    ELSE 1 
                END as max_attendees,
                g.id as primary_contact_guest_id,
                CURRENT_TIMESTAMP as created_at
            FROM guests g
            WHERE g.family_group_id IS NULL
              AND NOT EXISTS (
                  SELECT 1 FROM family_groups fg WHERE fg.primary_contact_guest_id = g.id
              )
            """;
        executeIfNotExists(createFamilyGroupsSQL);

        // Update guests to link them to their family groups and mark as primary contacts
        String linkGuestsToFamilyGroupsSQL = """
            UPDATE guests 
            SET 
                family_group_id = fg.id,
                is_primary_contact = true
            FROM family_groups fg
            WHERE guests.id = fg.primary_contact_guest_id
              AND guests.family_group_id IS NULL
            """;
        executeIfNotExists(linkGuestsToFamilyGroupsSQL);

        log.info("Guest migration to family system completed");
    }

    private void executeIfNotExists(String sql) {
        try {
            jdbcTemplate.execute(sql);
            log.debug("Successfully executed SQL statement");
        } catch (Exception e) {
            log.debug("SQL statement skipped (likely already exists): {}", e.getMessage());
        }
    }

    // Helper method for manual family group creation (can be called from controllers)
    public void createFamilyGroup(String groupName, int maxAttendees, Long primaryContactGuestId) {
        log.info("Creating family group: {} with max attendees: {} for guest: {}", 
                groupName, maxAttendees, primaryContactGuestId);

        String createGroupSQL = """
            INSERT INTO family_groups (group_name, max_attendees, primary_contact_guest_id, created_at)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            """;

        String updateGuestSQL = """
            UPDATE guests 
            SET family_group_id = (
                SELECT id FROM family_groups 
                WHERE primary_contact_guest_id = ? 
                ORDER BY created_at DESC 
                LIMIT 1
            ), is_primary_contact = true
            WHERE id = ?
            """;

        try {
            jdbcTemplate.update(createGroupSQL, groupName, maxAttendees, primaryContactGuestId);
            jdbcTemplate.update(updateGuestSQL, primaryContactGuestId, primaryContactGuestId);
            log.info("Successfully created family group: {}", groupName);
        } catch (Exception e) {
            log.error("Error creating family group: {}", groupName, e);
        }
    }

    // Helper method to add additional guests to existing family group
    public void addGuestToFamilyGroup(Long guestId, Long familyGroupId, boolean isPrimaryContact) {
        log.info("Adding guest {} to family group {} (primary contact: {})", 
                guestId, familyGroupId, isPrimaryContact);

        String updateGuestSQL = """
            UPDATE guests 
            SET family_group_id = ?, is_primary_contact = ?
            WHERE id = ?
            """;

        try {
            jdbcTemplate.update(updateGuestSQL, familyGroupId, isPrimaryContact, guestId);
            log.info("Successfully added guest {} to family group {}", guestId, familyGroupId);
        } catch (Exception e) {
            log.error("Error adding guest {} to family group {}", guestId, familyGroupId, e);
        }
    }
}
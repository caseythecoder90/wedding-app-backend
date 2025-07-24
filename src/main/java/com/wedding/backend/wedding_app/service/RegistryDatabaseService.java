package com.wedding.backend.wedding_app.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class RegistryDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(RegistryDatabaseService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeRegistryDatabase() {
        log.info("BEGIN - Initializing registry database indexes and constraints");

        try {
            createRegistryIndexes();
            createRegistryConstraints();
            insertDefaultData();
            log.info("END - Registry database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during registry database initialization", e);
        }
    }

    private void createRegistryIndexes() {
        log.info("Creating registry database indexes...");

        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_status ON donations(status)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_payment_method ON donations(payment_method)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_guest_id ON donations(guest_id)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_donor_email ON donations(donor_email)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_donation_date ON donations(donation_date)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_donations_confirmed_date ON donations(confirmed_date)");
        executeIfNotExists("CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_thank_you_templates_name ON thank_you_templates(template_name)");

        // Unique indexes for business constraints
        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_indexes
                    WHERE indexname = 'idx_registry_settings_active'
                ) THEN
                    CREATE UNIQUE INDEX idx_registry_settings_active
                    ON registry_settings (is_active)
                    WHERE is_active = true;
                END IF;
            END $$
            """);

        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_indexes
                    WHERE indexname = 'idx_thank_you_templates_default'
                ) THEN
                    CREATE UNIQUE INDEX idx_thank_you_templates_default
                    ON thank_you_templates (is_default)
                    WHERE is_default = true;
                END IF;
            END $$
            """);
    }

    private void createRegistryConstraints() {
        log.info("Creating registry database constraints...");

        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint 
                    WHERE conname = 'check_positive_amount'
                ) THEN
                    ALTER TABLE donations ADD CONSTRAINT check_positive_amount CHECK (amount > 0);
                END IF;
            END $$
            """);

        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint
                    WHERE conname = 'check_positive_goal'
                ) THEN
                    ALTER TABLE registry_settings ADD CONSTRAINT check_positive_goal CHECK (honeymoon_goal_amount > 0);
                END IF;
            END $$
            """);

        executeIfNotExists("""
            DO $$
            BEGIN
                IF NOT EXISTS (
                    SELECT 1 FROM pg_constraint
                    WHERE conname = 'check_template_name_not_empty'
                ) THEN
                    ALTER TABLE thank_you_templates ADD CONSTRAINT check_template_name_not_empty CHECK (template_name != '');
                END IF;
            END $$
            """);
    }

    private void insertDefaultData() {
        log.info("Inserting default registry data...");

        // Insert default registry settings if none exist
        String defaultRegistrySQL = """
            INSERT INTO registry_settings (honeymoon_goal_amount, venmo_handle, zelle_handle, registry_description, is_active, created_at, updated_at)
            SELECT 5000.00, '@Yasmimks', 'casey.yasmim@email.com', 'Help us create amazing honeymoon memories!', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
            WHERE NOT EXISTS (SELECT 1 FROM registry_settings WHERE is_active = true)
            """;
        executeIfNotExists(defaultRegistrySQL);

        // Insert default thank you template if none exist
        String defaultTemplateSQL = """
            INSERT INTO thank_you_templates (template_name, subject, email_body, is_default, created_at, updated_at)
            SELECT 'Default Thank You',
                   'Thank you for your generous honeymoon contribution! 💕',
                   'Dear {donorName},

Thank you so much for your generous contribution of ${amount} to our honeymoon fund! Your thoughtfulness means the world to us.

{message}

We are so grateful to have friends and family like you who are helping make our dream honeymoon a reality. We cannot wait to share our adventures with you when we return!

With love and gratitude,
Casey & Yasmim

P.S. We will be sure to send you photos from our trip! 📸✈️',
                   true,
                   CURRENT_TIMESTAMP,
                   CURRENT_TIMESTAMP
            WHERE NOT EXISTS (SELECT 1 FROM thank_you_templates WHERE is_default = true)
            """;
        executeIfNotExists(defaultTemplateSQL);

        log.info("Default registry data insertion completed");
    }

    private void executeIfNotExists(String sql) {
        try {
            jdbcTemplate.execute(sql);
            log.debug("Successfully executed SQL statement");
        } catch (Exception e) {
            log.debug("SQL statement skipped (likely already exists): {}", e.getMessage());
        }
    }
}
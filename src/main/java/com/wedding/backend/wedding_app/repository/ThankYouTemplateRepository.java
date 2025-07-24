package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.ThankYouTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThankYouTemplateRepository extends JpaRepository<ThankYouTemplateEntity, Long> {

    /**
     * Find the default thank you template
     */
    @Query("SELECT t FROM ThankYouTemplateEntity t WHERE t.isDefault = true")
    Optional<ThankYouTemplateEntity> findDefaultTemplate();

    /**
     * Find template by name (case-insensitive)
     */
    @Query("SELECT t FROM ThankYouTemplateEntity t WHERE LOWER(t.templateName) = LOWER(:templateName)")
    Optional<ThankYouTemplateEntity> findByTemplateNameIgnoreCase(String templateName);

    /**
     * Find all templates ordered by name
     */
    List<ThankYouTemplateEntity> findAllByOrderByTemplateNameAsc();

    /**
     * Check if a default template exists
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM ThankYouTemplateEntity t WHERE t.isDefault = true")
    boolean existsDefaultTemplate();

    /**
     * Count templates with a specific name (for uniqueness check)
     */
    @Query("SELECT COUNT(t) FROM ThankYouTemplateEntity t WHERE LOWER(t.templateName) = LOWER(:templateName)")
    int countByTemplateNameIgnoreCase(String templateName);
}
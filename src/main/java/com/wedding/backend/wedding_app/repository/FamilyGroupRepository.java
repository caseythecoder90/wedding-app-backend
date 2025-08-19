package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.FamilyGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyGroupRepository extends JpaRepository<FamilyGroupEntity, Long> {

    /**
     * Find family group by primary contact guest ID
     */
    Optional<FamilyGroupEntity> findByPrimaryContactId(Long primaryContactGuestId);

    /**
     * Find family groups by group name (case-insensitive)
     */
    List<FamilyGroupEntity> findByGroupNameIgnoreCase(String groupName);

    /**
     * Find all family groups with their primary contacts
     */
    @Query("SELECT fg FROM FamilyGroupEntity fg LEFT JOIN FETCH fg.primaryContact")
    List<FamilyGroupEntity> findAllWithPrimaryContacts();

    /**
     * Check if a guest is already a primary contact for any family group
     */
    boolean existsByPrimaryContactId(Long guestId);

    /**
     * Find family groups that have more than one attendee allowed
     */
    @Query("SELECT fg FROM FamilyGroupEntity fg WHERE fg.maxAttendees > 1")
    List<FamilyGroupEntity> findFamilyGroupsWithMultipleAttendees();

    /**
     * Count family groups by max attendees
     */
    @Query("SELECT COUNT(fg) FROM FamilyGroupEntity fg WHERE fg.maxAttendees = :maxAttendees")
    long countByMaxAttendees(@Param("maxAttendees") Integer maxAttendees);
}
package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.FamilyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMemberEntity, Long> {

    /**
     * Find all family members by family group ID
     */
    List<FamilyMemberEntity> findByFamilyGroupId(Long familyGroupId);

    /**
     * Find family members by family group ID and attendance status
     */
    List<FamilyMemberEntity> findByFamilyGroupIdAndIsAttending(Long familyGroupId, Boolean isAttending);

    /**
     * Find family members by age group
     */
    List<FamilyMemberEntity> findByAgeGroup(String ageGroup);

    /**
     * Find family members by first and last name in a specific family group
     */
    List<FamilyMemberEntity> findByFamilyGroupIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
        Long familyGroupId, String firstName, String lastName);

    /**
     * Count family members by family group ID
     */
    long countByFamilyGroupId(Long familyGroupId);

    /**
     * Count attending family members by family group ID
     */
    @Query("SELECT COUNT(fm) FROM FamilyMemberEntity fm WHERE fm.familyGroup.id = :familyGroupId AND fm.isAttending = true")
    long countAttendingByFamilyGroupId(@Param("familyGroupId") Long familyGroupId);

    /**
     * Find all family members with dietary restrictions
     */
    @Query("SELECT fm FROM FamilyMemberEntity fm WHERE fm.dietaryRestrictions IS NOT NULL AND fm.dietaryRestrictions != ''")
    List<FamilyMemberEntity> findAllWithDietaryRestrictions();

    /**
     * Delete all family members by family group ID
     */
    void deleteByFamilyGroupId(Long familyGroupId);

    /**
     * Find family members by age group and attendance status
     */
    @Query("SELECT fm FROM FamilyMemberEntity fm WHERE fm.ageGroup = :ageGroup AND fm.isAttending = :isAttending")
    List<FamilyMemberEntity> findByAgeGroupAndAttendanceStatus(@Param("ageGroup") String ageGroup, @Param("isAttending") Boolean isAttending);

    /**
     * Find all family members who are attending
     */
    @Query("SELECT fm FROM FamilyMemberEntity fm WHERE fm.isAttending = true ORDER BY fm.familyGroup.groupName, fm.firstName, fm.lastName")
    List<FamilyMemberEntity> findAllAttending();
}
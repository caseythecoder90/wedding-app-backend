package com.wedding.backend.wedding_app.repository;

import com.wedding.backend.wedding_app.entity.DonationEntity;
import com.wedding.backend.wedding_app.enums.DonationStatus;
import com.wedding.backend.wedding_app.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    /**
     * Get the total amount of confirmed donations
     */
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM DonationEntity d WHERE d.status = 'CONFIRMED'")
    BigDecimal getTotalConfirmedDonations();

    /**
     * Count donations by status
     */
    @Query("SELECT COUNT(d) FROM DonationEntity d WHERE d.status = :status")
    Integer countDonationsByStatus(@Param("status") DonationStatus status);

    /**
     * Get average donation amount for confirmed donations
     */
    @Query("SELECT COALESCE(AVG(d.amount), 0) FROM DonationEntity d WHERE d.status = 'CONFIRMED'")
    BigDecimal getAverageDonationAmount();

    /**
     * Find donations by status ordered by donation date (newest first)
     */
    List<DonationEntity> findByStatusOrderByDonationDateDesc(DonationStatus status);

    /**
     * Find all donations ordered by donation date (newest first)
     */
    List<DonationEntity> findAllByOrderByDonationDateDesc();

    /**
     * Find donations by guest ID ordered by donation date (newest first)
     */
    List<DonationEntity> findByGuestIdOrderByDonationDateDesc(Long guestId);

    /**
     * Find confirmed donations that haven't had 'thank you' emails sent
     */
    @Query("SELECT d FROM DonationEntity d WHERE d.status = 'CONFIRMED' AND d.thankYouSentDate IS NULL")
    List<DonationEntity> findConfirmedDonationsWithoutThankYou();

    /**
     * Find donations by payment method
     */
    List<DonationEntity> findByPaymentMethodOrderByDonationDateDesc(PaymentMethod paymentMethod);

    /**
     * Find donations within a date range
     */
    @Query("SELECT d FROM DonationEntity d WHERE d.donationDate BETWEEN :startDate AND :endDate ORDER BY d.donationDate DESC")
    List<DonationEntity> findDonationsBetweenDates(@Param("startDate") OffsetDateTime startDate,
                                                   @Param("endDate") OffsetDateTime endDate);

    /**
     * Find donations by donor email (case-insensitive)
     */
    @Query("SELECT d FROM DonationEntity d WHERE LOWER(d.donorEmail) = LOWER(:email) ORDER BY d.donationDate DESC")
    List<DonationEntity> findByDonorEmailIgnoreCase(@Param("email") String email);

    /**
     * Check if a donation exists with the same donor email and amount within the last hour
     * (to prevent duplicate submissions)
     */
    @Query("SELECT d FROM DonationEntity d WHERE LOWER(d.donorEmail) = LOWER(:email) " +
            "AND d.amount = :amount AND d.donationDate > :oneHourAgo")
    Optional<DonationEntity> findRecentDuplicateDonation(@Param("email") String email,
                                                         @Param("amount") BigDecimal amount,
                                                         @Param("oneHourAgo") OffsetDateTime oneHourAgo);

    /**
     * Get donation statistics for a specific payment method
     */
    @Query("SELECT COUNT(d), COALESCE(SUM(d.amount), 0), COALESCE(AVG(d.amount), 0) " +
            "FROM DonationEntity d WHERE d.paymentMethod = :paymentMethod AND d.status = 'CONFIRMED'")
    Object[] getDonationStatsByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);
}
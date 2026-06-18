package com.ebenezer.modules.prayer.repository;

import com.ebenezer.modules.prayer.model.PrayerRequest;
import com.ebenezer.modules.user.model.UserEntity;
import com.ebenezer.shared.enums.PrayerRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, Long> {

    Page<PrayerRequest> findAllByStatus(PrayerRequestStatus status, Pageable pageable);
    Page<PrayerRequest> findAllByUser(UserEntity user, Pageable pageable);
    Page<PrayerRequest> findAllByUserAndStatus(UserEntity user, PrayerRequestStatus status, Pageable pageable);

    @Modifying
    @Query("UPDATE PrayerRequest p SET p.prayerCount = p.prayerCount + 1 WHERE p.id = :id")
    void incrementPrayerCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PrayerRequest p SET p.prayerCount = p.prayerCount - 1 WHERE p.id = :id AND p.prayerCount > 0")
    void decrementPrayerCount(@Param("id") Long id);

    Page<PrayerRequest> findAllByUserAndStatusIn(UserEntity user, java.util.List<PrayerRequestStatus> statuses, Pageable pageable);
}

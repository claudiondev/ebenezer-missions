package com.ebenezer.modules.prayer.repository;

import com.ebenezer.modules.prayer.model.Prayer;
import com.ebenezer.modules.prayer.model.PrayerRequest;
import com.ebenezer.modules.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrayerRepository extends JpaRepository<Prayer, Long> {
    boolean existsByUserAndPrayerRequest(UserEntity user, PrayerRequest prayerRequest);
    void deleteByUserAndPrayerRequest(UserEntity user, PrayerRequest prayerRequest);
}

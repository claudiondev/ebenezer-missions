package com.ebenezer.modules.prayer.repository;

import com.ebenezer.modules.prayer.model.Encouragement;
import com.ebenezer.modules.prayer.model.PrayerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncouragementRepository extends JpaRepository<Encouragement, Long> {
    List<Encouragement> findAllByPrayerRequest(PrayerRequest prayerRequest);
}

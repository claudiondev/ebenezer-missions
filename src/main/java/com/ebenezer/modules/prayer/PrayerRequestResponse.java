package com.ebenezer.modules.prayer;

import com.ebenezer.modules.prayer.model.PrayerRequest;

import java.time.LocalDateTime;

public record PrayerRequestResponse(
        Long id,
        String title,
        String description,
        String status,
        Integer prayerCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String userName
) {
    public static PrayerRequestResponse from(PrayerRequest pr) {
        return new PrayerRequestResponse(
                pr.getId(),
                pr.getTitle(),
                pr.getDescription(),
                pr.getStatus().name(),
                pr.getPrayerCount(),
                pr.getCreatedAt(),
                pr.getUpdatedAt(),
                pr.getUser().getId(),
                pr.getUser().getName()
        );
    }
}

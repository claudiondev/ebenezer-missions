package com.ebenezer.modules.user;

import com.ebenezer.modules.prayer.PrayerRequestResponse;
import com.ebenezer.modules.user.model.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public record MissionaryProfileResponse(
        Long id,
        String name,
        String field,
        String country,
        String bio,
        LocalDateTime memberSince,
        List<PrayerRequestResponse> openRequests
) {
    public static MissionaryProfileResponse from(UserEntity u, List<PrayerRequestResponse> requests) {
        return new MissionaryProfileResponse(
                u.getId(), u.getName(), u.getField(), u.getCountry(), u.getBio(), u.getCreatedAt(), requests
        );
    }
}

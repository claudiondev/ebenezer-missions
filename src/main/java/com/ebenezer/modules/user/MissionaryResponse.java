package com.ebenezer.modules.user;

import com.ebenezer.modules.user.model.UserEntity;

import java.time.LocalDateTime;

public record MissionaryResponse(
        Long id,
        String name,
        String field,
        String country,
        String bio,
        LocalDateTime memberSince
) {
    public static MissionaryResponse from(UserEntity u) {
        return new MissionaryResponse(u.getId(), u.getName(), u.getField(), u.getCountry(), u.getBio(), u.getCreatedAt());
    }
}

package com.ebenezer.modules.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long id,
        String name,
        String email,
        String role
) {}

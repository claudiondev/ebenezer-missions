package com.ebenezer.modules.auth;

import com.ebenezer.config.JwtProperties;
import com.ebenezer.modules.auth.model.RefreshToken;
import com.ebenezer.modules.auth.repository.RefreshTokenRepository;
import com.ebenezer.modules.user.model.UserEntity;
import com.ebenezer.modules.user.service.UserService;
import com.ebenezer.shared.exception.BusinessException;
import com.ebenezer.shared.exception.ResourceNotFoundException;
import com.ebenezer.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        var user = UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(request.role())
                .field(request.field())
                .country(request.country())
                .bio(request.bio())
                .build();

        return buildAuthResponse(userService.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        UserEntity user;
        try {
            user = userService.findByEmail(request.email());
        } catch (ResourceNotFoundException e) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(String rawToken) {
        var stored = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(stored);
            throw new UnauthorizedException("Refresh token expirado");
        }

        var user = stored.getUser();
        refreshTokenRepository.delete(stored);
        return buildAuthResponse(user);
    }

    @Transactional
    public void logout(String email) {
        var user = userService.findByEmail(email);
        refreshTokenRepository.deleteByUser(user);
    }

    private AuthResponse buildAuthResponse(UserEntity user) {
        var accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        var refreshTokenValue = jwtUtil.generateRefreshToken(user.getEmail());

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        var expiresAt = LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS);
        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(expiresAt)
                .build());

        return new AuthResponse(
                accessToken,
                refreshTokenValue,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}

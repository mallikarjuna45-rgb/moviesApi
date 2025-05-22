package com.cm.movieapi.auth.service;

import com.cm.movieapi.Exception.TokenExpiredException;
import com.cm.movieapi.Exception.TokenNotFoundException;
import com.cm.movieapi.auth.entities.RefreshToken;
import com.cm.movieapi.auth.entities.User;
import com.cm.movieapi.auth.repositories.RefreshTokenRepository;
import com.cm.movieapi.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));
        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            long refreshTokenValidity = 30 * 100000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(userRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username)))
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("refresh token not exist"));

        if (refreshTokenOb.getExpirationTime().compareTo(Instant.now()) < 0) {
            System.out.println("Entered here ....");
            refreshTokenRepository.delete(refreshTokenOb);
            throw new TokenExpiredException("Refresh Token expired");
        }

        return refreshTokenOb;
    }
}
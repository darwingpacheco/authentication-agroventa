package com.agroventa.authentication.domain.model;

public record AuthToken(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}


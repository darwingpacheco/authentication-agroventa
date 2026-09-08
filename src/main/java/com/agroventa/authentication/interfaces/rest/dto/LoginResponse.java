package com.agroventa.authentication.interfaces.rest.dto;

import com.agroventa.authentication.domain.model.AuthToken;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {

    public static LoginResponse fromDomain(AuthToken authToken) {
        return new LoginResponse(authToken.accessToken(), authToken.tokenType(), authToken.expiresInSeconds());
    }
}


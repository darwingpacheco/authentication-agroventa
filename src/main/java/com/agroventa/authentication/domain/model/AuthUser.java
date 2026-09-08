package com.agroventa.authentication.domain.model;

import java.util.List;
import java.util.UUID;

public record AuthUser(
        UUID id,
        String email,
        String passwordHash,
        boolean enabled,
        List<String> roles
) {
}


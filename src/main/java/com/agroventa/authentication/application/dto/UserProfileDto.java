package com.agroventa.authentication.application.dto;

import java.util.List;
import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String name,
        String lastName,
        String phone,
        String email,
        List<String> roles,
        Boolean status
) {}

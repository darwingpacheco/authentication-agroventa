package com.agroventa.authentication.interfaces.rest.dto;

import com.agroventa.authentication.application.dto.UserProfileDto;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String name,
        String lastName,
        String phone,
        String email,
        List<String> roles
) {
    public static UserProfileResponse fromDto(UserProfileDto dto) {
        return new UserProfileResponse(
                dto.id(), dto.name(), dto.lastName(),
                dto.phone(), dto.email(), dto.roles());
    }
}

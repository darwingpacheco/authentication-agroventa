package com.agroventa.authentication.infrastructure.persistence.mapper;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.application.dto.UserProfileDto;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.infrastructure.persistence.entity.UserEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserMapper {

    private UserMapper() {}

    public static UserEntity requestToEntityUser(RegistryCommand register) {
        return UserEntity.builder()
                .name(register.name())
                .lastName(register.lastName())
                .phone(register.phone())
                .email(register.email())
                .passwordHash(register.password())
                .status(Boolean.TRUE)
                .roles(register.role() != null ? register.role() : "COMPRADOR")
                .build();
    }

    public static AuthUser entityToAuthUser(UserEntity entity) {
        return new AuthUser(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                Boolean.TRUE.equals(entity.getStatus()),
                parseRoles(entity.getRoles())
        );
    }

    public static UserProfileDto entityToProfileDto(UserEntity entity) {
        return new UserProfileDto(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getEmail(),
                parseRoles(entity.getRoles()),
                entity.getStatus()
        );
    }

    public static List<String> parseRoles(String roles) {
        if (roles == null || roles.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(v -> !v.isBlank())
                .toList();
    }
}

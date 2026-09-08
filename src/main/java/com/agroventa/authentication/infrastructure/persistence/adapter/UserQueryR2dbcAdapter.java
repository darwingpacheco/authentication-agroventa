package com.agroventa.authentication.infrastructure.persistence.adapter;

import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.port.UserQueryPort;
import com.agroventa.authentication.infrastructure.persistence.entity.UserEntity;
import com.agroventa.authentication.infrastructure.persistence.repository.UserR2dbcRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserQueryR2dbcAdapter implements UserQueryPort {

    private final UserR2dbcRepository userR2dbcRepository;

    public UserQueryR2dbcAdapter(UserR2dbcRepository userR2dbcRepository) {
        this.userR2dbcRepository = userR2dbcRepository;
    }

    @Override
    public Mono<AuthUser> findByEmail(String email) {
        return userR2dbcRepository.findByEmailIgnoreCase(email)
                .map(this::toDomain);
    }

    private AuthUser toDomain(UserEntity entity) {
        return new AuthUser(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                Boolean.TRUE.equals(entity.getEnabled()),
                parseRoles(entity.getRoles())
        );
    }

    private List<String> parseRoles(String roles) {
        if (roles == null || roles.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
    }
}


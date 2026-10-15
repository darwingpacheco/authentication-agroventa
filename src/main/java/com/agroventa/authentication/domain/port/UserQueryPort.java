package com.agroventa.authentication.domain.port;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.application.dto.UserProfileDto;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.model.RegistryUser;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserQueryPort {

    Mono<AuthUser> findByEmail(String email);

    Mono<UserProfileDto> findProfileById(UUID id);

    Mono<RegistryUser> createUser(RegistryCommand registryCommand);

    Mono<Void> updatePasswordHash(UUID userId, String newPasswordHash);
}

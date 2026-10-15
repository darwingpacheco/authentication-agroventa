package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.UserProfileDto;
import com.agroventa.authentication.application.usecase.GetUserProfileUseCase;
import com.agroventa.authentication.domain.exception.ResourceNotFoundException;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GetUserProfileService implements GetUserProfileUseCase {

    private final UserQueryPort userQueryPort;

    public GetUserProfileService(UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    @Override
    public Mono<UserProfileDto> getProfile(UUID userId) {
        return userQueryPort.findProfileById(userId)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Usuario no encontrado")));
    }
}

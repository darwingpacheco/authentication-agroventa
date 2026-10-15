package com.agroventa.authentication.application.usecase;

import com.agroventa.authentication.application.dto.UserProfileDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetUserProfileUseCase {
    Mono<UserProfileDto> getProfile(UUID userId);
}

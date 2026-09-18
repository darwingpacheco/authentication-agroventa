package com.agroventa.authentication.application.usecase;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.domain.model.RegistryUser;
import reactor.core.publisher.Mono;

public interface RegisterUseCase {

    Mono<RegistryUser> register(RegistryCommand registryCommand);
}

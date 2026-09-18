package com.agroventa.authentication.domain.port;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.model.RegistryUser;
import com.agroventa.authentication.interfaces.rest.dto.RegisterRequest;
import reactor.core.publisher.Mono;

public interface UserQueryPort {

    Mono<AuthUser> findByEmail(String email);

    Mono<RegistryUser> createUser(RegistryCommand registryCommand);
}


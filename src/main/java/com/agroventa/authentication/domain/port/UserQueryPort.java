package com.agroventa.authentication.domain.port;

import com.agroventa.authentication.domain.model.AuthUser;
import reactor.core.publisher.Mono;

public interface UserQueryPort {

    Mono<AuthUser> findByEmail(String email);
}


package com.agroventa.authentication.domain.port;

import com.agroventa.authentication.domain.model.AuthToken;
import com.agroventa.authentication.domain.model.AuthUser;
import reactor.core.publisher.Mono;

public interface TokenGeneratorPort {

    Mono<AuthToken> generate(AuthUser user);
}


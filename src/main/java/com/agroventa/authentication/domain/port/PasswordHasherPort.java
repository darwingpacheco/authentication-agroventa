package com.agroventa.authentication.domain.port;

import reactor.core.publisher.Mono;

public interface PasswordHasherPort {

    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}


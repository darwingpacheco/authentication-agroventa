package com.agroventa.authentication.application.usecase;

import com.agroventa.authentication.application.dto.LoginCommand;
import com.agroventa.authentication.domain.model.AuthToken;
import reactor.core.publisher.Mono;

public interface LoginUseCase {

    Mono<AuthToken> login(LoginCommand command);
}


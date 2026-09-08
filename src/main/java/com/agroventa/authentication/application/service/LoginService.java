package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.LoginCommand;
import com.agroventa.authentication.application.usecase.LoginUseCase;
import com.agroventa.authentication.domain.exception.InvalidCredentialsException;
import com.agroventa.authentication.domain.model.AuthToken;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.TokenGeneratorPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

public class LoginService implements LoginUseCase {

    private static final String INVALID_CREDENTIALS = "Invalid email or password";

    private final UserQueryPort userQueryPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    public LoginService(
            UserQueryPort userQueryPort,
            PasswordHasherPort passwordHasherPort,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        this.userQueryPort = userQueryPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override
    public Mono<AuthToken> login(LoginCommand command) {
        return userQueryPort.findByEmail(command.email().trim().toLowerCase())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(INVALID_CREDENTIALS)))
                .filter(authUser -> authUser.enabled())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(INVALID_CREDENTIALS)))
                .filterWhen(authUser -> passwordHasherPort.matches(command.password(), authUser.passwordHash()))
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(INVALID_CREDENTIALS)))
                .flatMap(tokenGeneratorPort::generate);
    }
}


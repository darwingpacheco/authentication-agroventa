package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.application.usecase.RegisterUseCase;
import com.agroventa.authentication.domain.exception.InvalidCredentialsException;
import com.agroventa.authentication.domain.model.RegistryUser;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

public class RegisterService implements RegisterUseCase {

    private final UserQueryPort userQueryPort;
    private final PasswordHasherPort passwordHasherPort;

    public RegisterService(UserQueryPort userQueryPort, PasswordHasherPort passwordHasherPort) {
        this.userQueryPort = userQueryPort;
        this.passwordHasherPort = passwordHasherPort;
    }

    @Override
    public Mono<RegistryUser> register(RegistryCommand registryCommand) {
        return userQueryPort.findByEmail(registryCommand.email())
                .flatMap(existingUser ->
                        Mono.<RegistryUser>error(new InvalidCredentialsException("Usuario ya registrado")))
                .switchIfEmpty(
                        passwordHasherPort.encryptPass(registryCommand.password())
                                .flatMap(passEncode -> {
                                    RegistryCommand reg = registryCommand.withPassword(passEncode);
                                    return userQueryPort.createUser(reg);
                                }));
    }
}

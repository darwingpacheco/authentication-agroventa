package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.application.usecase.RegisterUseCase;
import com.agroventa.authentication.domain.exception.InvalidCredentialsException;
import com.agroventa.authentication.domain.exception.UserAlreadyExistsException;
import com.agroventa.authentication.domain.model.RegistryUser;
import com.agroventa.authentication.domain.port.NotificationPort;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

public class RegisterService implements RegisterUseCase {

    private static final String ROLE_AGRICULTOR  = "AGRICULTOR";
    private static final String ROLE_COMPRADOR   = "COMPRADOR";

    private final UserQueryPort       userQueryPort;
    private final PasswordHasherPort  passwordHasherPort;
    private final NotificationPort    notificationPort;

    public RegisterService(
            UserQueryPort userQueryPort,
            PasswordHasherPort passwordHasherPort,
            NotificationPort notificationPort) {
        this.userQueryPort      = userQueryPort;
        this.passwordHasherPort = passwordHasherPort;
        this.notificationPort   = notificationPort;
    }

    @Override
    public Mono<RegistryUser> register(RegistryCommand registryCommand) {
        // Validate role
        String role = resolveRole(registryCommand.role());

        RegistryCommand commandWithRole = new RegistryCommand(
                registryCommand.name(),
                registryCommand.lastName(),
                registryCommand.phone(),
                registryCommand.email().trim().toLowerCase(),
                registryCommand.password(),
                role
        );

        return userQueryPort.findByEmail(commandWithRole.email())
                .flatMap(existing -> Mono.<RegistryUser>error(
                        new UserAlreadyExistsException("Email ya registrado")))
                .switchIfEmpty(
                        passwordHasherPort.encryptPass(commandWithRole.password())
                                .flatMap(hashedPass -> {
                                    RegistryCommand finalCmd = commandWithRole.withPassword(hashedPass);
                                    return userQueryPort.createUser(finalCmd)
                                            .flatMap(saved ->
                                                    notificationPort
                                                            .sendWelcomeEmail(commandWithRole.email(), commandWithRole.name())
                                                            // Don't fail registration if email sending fails
                                                            .onErrorResume(e -> Mono.empty())
                                                            .thenReturn(saved));
                                }));
    }

    private String resolveRole(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            return ROLE_COMPRADOR;
        }
        String upper = rawRole.toUpperCase().trim();
        if (!upper.equals(ROLE_AGRICULTOR) && !upper.equals(ROLE_COMPRADOR)) {
            throw new InvalidCredentialsException("Rol inválido. Usa AGRICULTOR o COMPRADOR");
        }
        return upper;
    }
}

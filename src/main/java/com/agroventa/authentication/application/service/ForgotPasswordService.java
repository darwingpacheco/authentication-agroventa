package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.ForgotPasswordCommand;
import com.agroventa.authentication.application.usecase.ForgotPasswordUseCase;
import com.agroventa.authentication.domain.port.NotificationPort;
import com.agroventa.authentication.domain.port.PasswordResetPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

public class ForgotPasswordService implements ForgotPasswordUseCase {

    private final UserQueryPort    userQueryPort;
    private final PasswordResetPort passwordResetPort;
    private final NotificationPort notificationPort;

    public ForgotPasswordService(
            UserQueryPort userQueryPort,
            PasswordResetPort passwordResetPort,
            NotificationPort notificationPort) {
        this.userQueryPort     = userQueryPort;
        this.passwordResetPort = passwordResetPort;
        this.notificationPort  = notificationPort;
    }

    @Override
    public Mono<Void> requestPasswordReset(ForgotPasswordCommand command) {
        String email = command.email().trim().toLowerCase();

        // We intentionally do NOT reveal whether the email exists (anti-enumeration)
        return userQueryPort.findByEmail(email)
                .flatMap(authUser ->
                        passwordResetPort.createCode(authUser.id())
                                .flatMap(plainCode ->
                                        notificationPort.sendPasswordResetCode(
                                                email, authUser.email(), plainCode))
                                .onErrorResume(e -> Mono.empty()))
                .then();
    }
}

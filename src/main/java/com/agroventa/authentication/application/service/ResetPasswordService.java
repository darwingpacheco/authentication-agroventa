package com.agroventa.authentication.application.service;

import com.agroventa.authentication.application.dto.ResetPasswordCommand;
import com.agroventa.authentication.application.usecase.ResetPasswordUseCase;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.PasswordResetPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import reactor.core.publisher.Mono;

public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserQueryPort    userQueryPort;
    private final PasswordResetPort passwordResetPort;
    private final PasswordHasherPort passwordHasherPort;

    public ResetPasswordService(
            UserQueryPort userQueryPort,
            PasswordResetPort passwordResetPort,
            PasswordHasherPort passwordHasherPort) {
        this.userQueryPort      = userQueryPort;
        this.passwordResetPort  = passwordResetPort;
        this.passwordHasherPort = passwordHasherPort;
    }

    @Override
    public Mono<Void> resetPassword(ResetPasswordCommand command) {
        String email = command.email().trim().toLowerCase();

        return passwordResetPort.validateCode(email, command.code())
                .flatMap(userId ->
                        passwordHasherPort.encryptPass(command.newPassword())
                                .flatMap(hashedPass ->
                                        userQueryPort.updatePasswordHash(userId, hashedPass)
                                                .then(passwordResetPort.markUsed(userId, command.code()))));
    }
}

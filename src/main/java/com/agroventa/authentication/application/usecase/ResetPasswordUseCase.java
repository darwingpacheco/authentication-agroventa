package com.agroventa.authentication.application.usecase;

import com.agroventa.authentication.application.dto.ResetPasswordCommand;
import reactor.core.publisher.Mono;

public interface ResetPasswordUseCase {
    Mono<Void> resetPassword(ResetPasswordCommand command);
}

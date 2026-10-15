package com.agroventa.authentication.application.usecase;

import com.agroventa.authentication.application.dto.ForgotPasswordCommand;
import reactor.core.publisher.Mono;

public interface ForgotPasswordUseCase {
    /** Always returns Mono.empty() to avoid email enumeration. */
    Mono<Void> requestPasswordReset(ForgotPasswordCommand command);
}

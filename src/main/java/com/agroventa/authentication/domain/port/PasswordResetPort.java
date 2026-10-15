package com.agroventa.authentication.domain.port;

import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Port for managing password-reset codes.
 * Implementations handle persistence of the hashed 6-digit code.
 */
public interface PasswordResetPort {

    /** Saves a new reset code for the given user. Returns the plain-text code (not hashed). */
    Mono<String> createCode(UUID userId);

    /**
     * Validates the plain-text code against the stored hash.
     * Returns userId if valid, or an error if expired / already used / not found.
     */
    Mono<UUID> validateCode(String email, String plainCode);

    /** Marks the code as used so it cannot be reused. */
    Mono<Void> markUsed(UUID userId, String plainCode);
}

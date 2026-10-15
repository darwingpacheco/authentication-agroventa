package com.agroventa.authentication.domain.port;

import reactor.core.publisher.Mono;

/**
 * Port for sending notifications (email via SQS → Lambda → SES).
 */
public interface NotificationPort {

    /** Sends a welcome email when a new user registers. */
    Mono<Void> sendWelcomeEmail(String toEmail, String firstName);

    /** Sends a password-reset code email. */
    Mono<Void> sendPasswordResetCode(String toEmail, String firstName, String code);
}

package com.agroventa.authentication.infrastructure.persistence.repository;

import com.agroventa.authentication.infrastructure.persistence.entity.PasswordResetCodeEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface PasswordResetCodeR2dbcRepository
        extends ReactiveCrudRepository<PasswordResetCodeEntity, UUID> {

    /** Most recent valid (not used, not expired) code for a user */
    @Query("""
            SELECT * FROM password_reset_codes
            WHERE user_id = :userId
              AND used = FALSE
              AND expires_at > :now
            ORDER BY created_at DESC
            LIMIT 1
            """)
    Mono<PasswordResetCodeEntity> findLatestValidByUserId(UUID userId, OffsetDateTime now);

    /** Mark all active codes for a user as used (e.g., after successful reset) */
    @Modifying
    @Query("UPDATE password_reset_codes SET used = TRUE WHERE user_id = :userId AND used = FALSE")
    Mono<Void> markAllUsedForUser(UUID userId);
}

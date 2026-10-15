package com.agroventa.authentication.infrastructure.persistence.adapter;

import com.agroventa.authentication.domain.exception.InvalidCodeException;
import com.agroventa.authentication.domain.port.PasswordResetPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import com.agroventa.authentication.infrastructure.persistence.entity.PasswordResetCodeEntity;
import com.agroventa.authentication.infrastructure.persistence.repository.PasswordResetCodeR2dbcRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class PasswordResetR2dbcAdapter implements PasswordResetPort {

    private static final int CODE_TTL_MINUTES  = 15;
    private static final int MAX_ATTEMPTS      = 3;

    private final PasswordResetCodeR2dbcRepository repository;
    private final UserQueryPort                    userQueryPort;
    private final PasswordEncoder                  passwordEncoder;
    private final SecureRandom                     secureRandom = new SecureRandom();

    public PasswordResetR2dbcAdapter(
            PasswordResetCodeR2dbcRepository repository,
            UserQueryPort userQueryPort,
            PasswordEncoder passwordEncoder) {
        this.repository      = repository;
        this.userQueryPort   = userQueryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<String> createCode(UUID userId) {
        // Invalidate any existing active codes for this user first
        return repository.markAllUsedForUser(userId)
                .then(Mono.fromCallable(this::generateSixDigitCode)
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(plainCode -> {
                    String hashed = passwordEncoder.encode(plainCode);
                    PasswordResetCodeEntity entity = PasswordResetCodeEntity.builder()
                            .userId(userId)
                            .codeHash(hashed)
                            .attempts((short) 0)
                            .used(false)
                            .expiresAt(OffsetDateTime.now().plusMinutes(CODE_TTL_MINUTES))
                            .build();
                    return repository.save(entity)
                            .thenReturn(plainCode);
                });
    }

    @Override
    public Mono<UUID> validateCode(String email, String plainCode) {
        return userQueryPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCodeException("Código inválido o expirado")))
                .flatMap(authUser ->
                        repository.findLatestValidByUserId(authUser.id(), OffsetDateTime.now())
                                .switchIfEmpty(Mono.error(new InvalidCodeException("Código inválido o expirado")))
                                .flatMap(entity -> {
                                    if (entity.getAttempts() >= MAX_ATTEMPTS) {
                                        return Mono.error(new InvalidCodeException("Demasiados intentos. Solicita un nuevo código."));
                                    }
                                    return Mono.fromCallable(() -> passwordEncoder.matches(plainCode, entity.getCodeHash()))
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .flatMap(matches -> {
                                                if (!matches) {
                                                    // Increment attempt counter
                                                    entity.setAttempts((short) (entity.getAttempts() + 1));
                                                    return repository.save(entity)
                                                            .then(Mono.error(new InvalidCodeException("Código inválido o expirado")));
                                                }
                                                return Mono.just(authUser.id());
                                            });
                                }));
    }

    @Override
    public Mono<Void> markUsed(UUID userId, String plainCode) {
        return repository.markAllUsedForUser(userId);
    }

    private String generateSixDigitCode() {
        int code = 100_000 + secureRandom.nextInt(900_000);
        return String.valueOf(code);
    }
}

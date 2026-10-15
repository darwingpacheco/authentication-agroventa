package com.agroventa.authentication.infrastructure.persistence.adapter;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.application.dto.UserProfileDto;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.model.RegistryUser;
import com.agroventa.authentication.domain.port.UserQueryPort;
import com.agroventa.authentication.infrastructure.persistence.mapper.UserMapper;
import com.agroventa.authentication.infrastructure.persistence.repository.UserR2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserQueryR2dbcAdapter implements UserQueryPort {

    private final UserR2dbcRepository userR2dbcRepository;
    private final DatabaseClient      databaseClient;

    public UserQueryR2dbcAdapter(
            UserR2dbcRepository userR2dbcRepository,
            DatabaseClient databaseClient) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.databaseClient      = databaseClient;
    }

    @Override
    public Mono<AuthUser> findByEmail(String email) {
        return userR2dbcRepository.findByEmailIgnoreCase(email)
                .map(UserMapper::entityToAuthUser);
    }

    @Override
    public Mono<UserProfileDto> findProfileById(UUID id) {
        return userR2dbcRepository.findById(id)
                .map(UserMapper::entityToProfileDto);
    }

    @Override
    public Mono<RegistryUser> createUser(RegistryCommand registryCommand) {
        return userR2dbcRepository.save(UserMapper.requestToEntityUser(registryCommand))
                .map(entity -> new RegistryUser(entity.getId(), entity.getStatus()));
    }

    @Override
    public Mono<Void> updatePasswordHash(UUID userId, String newPasswordHash) {
        return databaseClient.sql(
                        "UPDATE users SET password_hash = :hash, updated_at = NOW() WHERE id = :id")
                .bind("hash", newPasswordHash)
                .bind("id", userId)
                .fetch()
                .rowsUpdated()
                .then();
    }
}

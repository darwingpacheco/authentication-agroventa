package com.agroventa.authentication.infrastructure.persistence.repository;

import com.agroventa.authentication.infrastructure.persistence.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, UUID> {

    Mono<UserEntity> findByEmailIgnoreCase(String email);
}


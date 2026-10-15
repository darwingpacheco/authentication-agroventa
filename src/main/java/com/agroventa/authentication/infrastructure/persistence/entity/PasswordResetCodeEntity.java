package com.agroventa.authentication.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("password_reset_codes")
public class PasswordResetCodeEntity {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("code_hash")
    private String codeHash;

    @Column("attempts")
    private Short attempts;

    @Column("used")
    private Boolean used;

    @Column("expires_at")
    private OffsetDateTime expiresAt;

    @Column("created_at")
    private OffsetDateTime createdAt;
}

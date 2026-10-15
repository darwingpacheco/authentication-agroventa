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
@Table("users")
public class UserEntity {

    @Id
    private UUID id;

    @Column("first_name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("password_hash")
    private String passwordHash;

    @Column("status")
    private Boolean status;

    @Column("roles")
    private String roles;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
}

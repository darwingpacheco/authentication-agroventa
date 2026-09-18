package com.agroventa.authentication.infrastructure.persistence.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
}


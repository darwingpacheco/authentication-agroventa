package com.agroventa.authentication.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull
        String name,
        @NotNull
        String lastName,
        @NotNull
        String phone,
        @NotNull
        @Email
        String email,
        @NotNull
        @Size(min = 8)
        String password
) {
}

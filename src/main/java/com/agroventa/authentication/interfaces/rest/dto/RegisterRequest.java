package com.agroventa.authentication.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String name,
        @NotBlank String lastName,
        @NotBlank @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Teléfono inválido") String phone,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 128) String password,
        @Pattern(regexp = "AGRICULTOR|COMPRADOR", flags = Pattern.Flag.CASE_INSENSITIVE,
                 message = "Rol debe ser AGRICULTOR o COMPRADOR")
        String role
) {}

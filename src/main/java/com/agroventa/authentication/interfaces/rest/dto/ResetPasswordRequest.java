package com.agroventa.authentication.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "\\d{6}", message = "El código debe ser de 6 dígitos numéricos") String code,
        @NotBlank @Size(min = 8, max = 128) String newPassword
) {}

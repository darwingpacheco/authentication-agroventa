package com.agroventa.authentication.application.dto;

public record LoginCommand(
        String email,
        String password
) {
}


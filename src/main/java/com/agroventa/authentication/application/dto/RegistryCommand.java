package com.agroventa.authentication.application.dto;

public record RegistryCommand(
        String name,
        String lastName,
        String phone,
        String email,
        String password,
        String role
) {
    public RegistryCommand withPassword(String newPassword) {
        return new RegistryCommand(name, lastName, phone, email, newPassword, role);
    }
}

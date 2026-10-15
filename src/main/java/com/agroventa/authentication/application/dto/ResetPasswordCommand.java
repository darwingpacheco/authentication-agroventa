package com.agroventa.authentication.application.dto;

public record ResetPasswordCommand(String email, String code, String newPassword) {}

package com.agroventa.authentication.domain.model;

import java.util.UUID;

public record RegistryUser(
        UUID id, boolean status
) {
}

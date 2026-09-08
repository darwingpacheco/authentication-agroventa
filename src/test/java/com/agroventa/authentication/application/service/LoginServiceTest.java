package com.agroventa.authentication.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.agroventa.authentication.application.dto.LoginCommand;
import com.agroventa.authentication.domain.exception.InvalidCredentialsException;
import com.agroventa.authentication.domain.model.AuthToken;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.TokenGeneratorPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserQueryPort userQueryPort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @Mock
    private TokenGeneratorPort tokenGeneratorPort;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(userQueryPort, passwordHasherPort, tokenGeneratorPort);
    }

    @Test
    void shouldLoginSuccessfully() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "user@agroventa.com",
                "encoded-password",
                true,
                List.of("ROLE_USER")
        );
        AuthToken token = new AuthToken("jwt-token", "Bearer", 3600);

        when(userQueryPort.findByEmail("user@agroventa.com")).thenReturn(Mono.just(user));
        when(passwordHasherPort.matches("ChangeMe123!", "encoded-password")).thenReturn(Mono.just(true));
        when(tokenGeneratorPort.generate(any(AuthUser.class))).thenReturn(Mono.just(token));

        StepVerifier.create(loginService.login(new LoginCommand("user@agroventa.com", "ChangeMe123!")))
                .expectNext(token)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenPasswordDoesNotMatch() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "user@agroventa.com",
                "encoded-password",
                true,
                List.of("ROLE_USER")
        );

        when(userQueryPort.findByEmail("user@agroventa.com")).thenReturn(Mono.just(user));
        when(passwordHasherPort.matches("wrong-password", "encoded-password")).thenReturn(Mono.just(false));

        StepVerifier.create(loginService.login(new LoginCommand("user@agroventa.com", "wrong-password")))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
}


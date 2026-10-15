package com.agroventa.authentication.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.agroventa.authentication.application.usecase.ForgotPasswordUseCase;
import com.agroventa.authentication.application.usecase.GetUserProfileUseCase;
import com.agroventa.authentication.application.usecase.LoginUseCase;
import com.agroventa.authentication.application.usecase.RegisterUseCase;
import com.agroventa.authentication.application.usecase.ResetPasswordUseCase;
import com.agroventa.authentication.domain.model.AuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class}
)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private RegisterUseCase registerUseCase;

    @MockBean
    private ForgotPasswordUseCase forgotPasswordUseCase;

    @MockBean
    private ResetPasswordUseCase resetPasswordUseCase;

    @MockBean
    private GetUserProfileUseCase getUserProfileUseCase;

    @Test
    void shouldLogin() {
        when(loginUseCase.login(any())).thenReturn(Mono.just(new AuthToken("jwt", "Bearer", 3600)));

        webTestClient.post()
                .uri("/api/agro-venta/v1/auth/login") // 👈 Se ajustó el URI correcto
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"email\":\"user@agroventa.com\",\"password\":\"ChangeMe123!\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").isEqualTo("jwt")
                .jsonPath("$.tokenType").isEqualTo("Bearer")
                .jsonPath("$.expiresInSeconds").isEqualTo(3600);
    }
}
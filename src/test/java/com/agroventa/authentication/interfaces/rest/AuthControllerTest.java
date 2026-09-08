package com.agroventa.authentication.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.agroventa.authentication.application.usecase.LoginUseCase;
import com.agroventa.authentication.domain.model.AuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = AuthController.class)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LoginUseCase loginUseCase;

    @Test
    void shouldLogin() {
        when(loginUseCase.login(any())).thenReturn(Mono.just(new AuthToken("jwt", "Bearer", 3600)));

        webTestClient.post()
                .uri("/api/v1/auth/login")
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


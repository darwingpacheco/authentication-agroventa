package com.agroventa.authentication.interfaces.rest;

import com.agroventa.authentication.application.dto.LoginCommand;
import com.agroventa.authentication.application.usecase.LoginUseCase;
import com.agroventa.authentication.interfaces.rest.dto.LoginRequest;
import com.agroventa.authentication.interfaces.rest.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/api/agro-venta/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody Mono<LoginRequest> requestMono) {
        return requestMono
                .map(request -> new LoginCommand(request.email(), request.password()))
                .flatMap(loginUseCase::login)
                .map(LoginResponse::fromDomain)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/capabilities")
    public Flux<String> capabilities() {
        return Flux.just("LOGIN", "JWT", "WEBFLUX", "HEXAGONAL_ARCHITECTURE");
    }
}


package com.agroventa.authentication.interfaces.rest;

import com.agroventa.authentication.application.dto.*;
import com.agroventa.authentication.application.usecase.*;
import com.agroventa.authentication.interfaces.rest.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/api/agro-venta/v1/auth")
public class AuthController {

    private final LoginUseCase          loginUseCase;
    private final RegisterUseCase       registerUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase  resetPasswordUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;

    public AuthController(
            LoginUseCase loginUseCase,
            RegisterUseCase registerUseCase,
            ForgotPasswordUseCase forgotPasswordUseCase,
            ResetPasswordUseCase resetPasswordUseCase,
            GetUserProfileUseCase getUserProfileUseCase) {
        this.loginUseCase          = loginUseCase;
        this.registerUseCase       = registerUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase  = resetPasswordUseCase;
        this.getUserProfileUseCase = getUserProfileUseCase;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(
            @Valid @RequestBody Mono<LoginRequest> requestMono) {
        return requestMono
                .map(req -> new LoginCommand(req.email(), req.password()))
                .flatMap(loginUseCase::login)
                .map(LoginResponse::fromDomain)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponse>> register(
            @Valid @RequestBody Mono<RegisterRequest> requestMono) {
        return requestMono
                .map(req -> new RegistryCommand(
                        req.name(), req.lastName(), req.phone(),
                        req.email(), req.password(), req.role()))
                .flatMap(registerUseCase::register)
                .map(user -> new RegisterResponse(user.id(), user.status()))
                .map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp));
    }

    /** Step 1 of password recovery: request a 6-digit code by email */
    @PostMapping("/forgot-password")
    public Mono<ResponseEntity<Void>> forgotPassword(
            @Valid @RequestBody Mono<ForgotPasswordRequest> requestMono) {
        return requestMono
                .map(req -> new ForgotPasswordCommand(req.email()))
                .flatMap(forgotPasswordUseCase::requestPasswordReset)
                .then(Mono.just(ResponseEntity.<Void>ok().build()));
    }

    /** Step 2 of password recovery: submit the 6-digit code and new password */
    @PostMapping("/reset-password")
    public Mono<ResponseEntity<Void>> resetPassword(
            @Valid @RequestBody Mono<ResetPasswordRequest> requestMono) {
        return requestMono
                .map(req -> new ResetPasswordCommand(req.email(), req.code(), req.newPassword()))
                .flatMap(resetPasswordUseCase::resetPassword)
                .then(Mono.just(ResponseEntity.<Void>ok().build()));
    }

    /**
     * Get current user profile.
     * The userId comes from the JWT validated externally by API Gateway or an upstream filter.
     * The caller passes X-User-Id header (injected by API Gateway after JWT authorizer).
     */
    @GetMapping("/me")
    public Mono<ResponseEntity<UserProfileResponse>> getMe(
            @RequestHeader("X-User-Id") String userId) {
        return getUserProfileUseCase.getProfile(java.util.UUID.fromString(userId))
                .map(profile -> ResponseEntity.ok(UserProfileResponse.fromDto(profile)));
    }

    @GetMapping("/capabilities")
    public Flux<String> capabilities() {
        return Flux.just("LOGIN", "REGISTER", "FORGOT_PASSWORD", "RESET_PASSWORD", "JWT", "WEBFLUX", "HEXAGONAL");
    }
}

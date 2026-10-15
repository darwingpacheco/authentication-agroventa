package com.agroventa.authentication.infrastructure.config;

import com.agroventa.authentication.application.service.*;
import com.agroventa.authentication.application.usecase.*;
import com.agroventa.authentication.domain.port.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(
            UserQueryPort userQueryPort,
            PasswordHasherPort passwordHasherPort,
            TokenGeneratorPort tokenGeneratorPort) {
        return new LoginService(userQueryPort, passwordHasherPort, tokenGeneratorPort);
    }

    @Bean
    public RegisterUseCase registerUseCase(
            UserQueryPort userQueryPort,
            PasswordHasherPort passwordHasherPort,
            NotificationPort notificationPort) {
        return new RegisterService(userQueryPort, passwordHasherPort, notificationPort);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(
            UserQueryPort userQueryPort,
            PasswordResetPort passwordResetPort,
            NotificationPort notificationPort) {
        return new ForgotPasswordService(userQueryPort, passwordResetPort, notificationPort);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            UserQueryPort userQueryPort,
            PasswordResetPort passwordResetPort,
            PasswordHasherPort passwordHasherPort) {
        return new ResetPasswordService(userQueryPort, passwordResetPort, passwordHasherPort);
    }

    @Bean
    public GetUserProfileUseCase getUserProfileUseCase(UserQueryPort userQueryPort) {
        return new GetUserProfileService(userQueryPort);
    }
}

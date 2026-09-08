package com.agroventa.authentication.infrastructure.config;

import com.agroventa.authentication.application.service.LoginService;
import com.agroventa.authentication.application.usecase.LoginUseCase;
import com.agroventa.authentication.domain.port.PasswordHasherPort;
import com.agroventa.authentication.domain.port.TokenGeneratorPort;
import com.agroventa.authentication.domain.port.UserQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(
            UserQueryPort userQueryPort,
            PasswordHasherPort passwordHasherPort,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        return new LoginService(userQueryPort, passwordHasherPort, tokenGeneratorPort);
    }
}


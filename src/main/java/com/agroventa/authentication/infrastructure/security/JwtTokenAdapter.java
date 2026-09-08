package com.agroventa.authentication.infrastructure.security;

import com.agroventa.authentication.domain.model.AuthToken;
import com.agroventa.authentication.domain.model.AuthUser;
import com.agroventa.authentication.domain.port.TokenGeneratorPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final JwtProperties jwtProperties;

    public JwtTokenAdapter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<AuthToken> generate(AuthUser user) {
        return Flux.fromIterable(user.roles())
                .collectList()
                .map(roles -> buildToken(user, roles));
    }

    private AuthToken buildToken(AuthUser user, List<String> roles) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusSeconds(jwtProperties.expirationSeconds());

        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(user.id().toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .claims(Map.of(
                        "email", user.email(),
                        "roles", roles
                ))
                .signWith(key)
                .compact();

        return new AuthToken(token, "Bearer", jwtProperties.expirationSeconds());
    }
}


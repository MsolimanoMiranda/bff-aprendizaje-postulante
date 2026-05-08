package pe.gob.oece.bff.config;

import java.time.Instant;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Profile({"local", "dev", "test"})
public class DevSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            Instant now = Instant.now();
            return Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "dev-user")
                .claim("roles", List.of("OECE_TECHOPS", "OECE_ANALYST"))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();
        };
    }
}
package pe.gob.oece.bff.config;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Profile({"local", "dev", "test"})
public class DevSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                com.nimbusds.jwt.JWT parsed = com.nimbusds.jwt.JWTParser.parse(token);
                Map<String, Object> claims = new HashMap<>(parsed.getJWTClaimsSet().getClaims());

                claims.remove("iss");
                claims.remove("aud");
                claims.remove("exp");
                claims.putIfAbsent("sub", "dev-user");
                claims.putIfAbsent("roles", List.of("OECE_TECHOPS", "OECE_ANALYST"));

                Instant now = Instant.now();
                return Jwt.withTokenValue(token)
                    .headers(headers -> headers.putAll(parsed.getHeader().toJSONObject()))
                    .claims(jwtClaims -> jwtClaims.putAll(claims))
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(3600))
                    .build();
            } catch (Exception ex) {
                throw new BadJwtException("Token invalido: " + ex.getMessage());
            }
        };
    }
}

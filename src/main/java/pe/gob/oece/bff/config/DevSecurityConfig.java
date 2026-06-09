package pe.gob.oece.bff.config;

import java.time.Instant;
import java.util.ArrayList;
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
//@Profile({"local", "dev", "test"})
public class DevSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try{
                com.nimbusds.jwt.JWT parsed = com.nimbusds.jwt.JWTParser.parse(token);
                java.util.Map<String, Object> claims = new java.util.HashMap<>(parsed.getJWTClaimsSet().getClaims());

                /// Deshabilitar validaciones para ambiente local
                claims.remove("iss");
                claims.remove("aud");
                claims.remove("exp");  // ← sin validar expiración tampoco

                Jwt jwtTemporal  = Jwt.withTokenValue(token)
                        .headers(h -> h.putAll(parsed.getHeader().toJSONObject()))
                        .claims(c -> c.putAll(claims))
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(3600))
                        .build();


                return org.springframework.security.oauth2.jwt.Jwt
                        .withTokenValue(token)
                        .headers(h -> h.putAll(parsed.getHeader().toJSONObject()))
                        .claims(c -> c.putAll(claims))
                        .issuedAt(java.time.Instant.now())
                        .expiresAt(java.time.Instant.now().plusSeconds(3600))
                        .build();
            }
            catch (Exception ex){
                throw new org.springframework.security.oauth2.jwt
                        .BadJwtException("Token inválido: " + ex.getMessage());
            }


        };
    }
}

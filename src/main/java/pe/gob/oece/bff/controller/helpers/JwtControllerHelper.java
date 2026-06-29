package pe.gob.oece.bff.controller.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

public final class JwtControllerHelper {

    private JwtControllerHelper() {
    }

    public static String obtenerToken(Jwt jwt) {
        if (jwt == null || jwt.getTokenValue() == null || jwt.getTokenValue().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        }
        return jwt.getTokenValue();
    }

    public static String obtenerToken(Jwt jwt, String authorization) {
        if (jwt != null && jwt.getTokenValue() != null && !jwt.getTokenValue().isBlank()) {
            return jwt.getTokenValue();
        }
        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        }
        return authorization.regionMatches(true, 0, "Bearer ", 0, 7)
                ? authorization.substring(7).trim()
                : authorization.trim();
    }

    public static String obtenerUsuario(Jwt jwt) {
        String usuario = jwt != null ? jwt.getClaimAsString("idUsuario") : null;
        if (usuario == null || usuario.isBlank()) {
            usuario = jwt != null ? jwt.getSubject() : null;
        }
        if (usuario == null || usuario.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sin usuario");
        }
        return usuario;
    }

    public static Long obtenerIdUsuarioGu(Jwt jwt) {
        String idUsuarioGu = jwt != null ? jwt.getClaimAsString("idUsuario") : null;

        if (idUsuarioGu == null || idUsuarioGu.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sin claim idUsuario");
        }

        try {
            return Long.valueOf(idUsuarioGu);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Claim idUsuario invalido");
        }
    }
}

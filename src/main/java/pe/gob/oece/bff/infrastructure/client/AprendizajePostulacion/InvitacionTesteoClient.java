package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.Map;

@Component
public class InvitacionTesteoClient {

    private static final String BASE_PATH = "/api/v1/invitacion-testeo";

    private final DownstreamWebClient http;

    public InvitacionTesteoClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-IT-EX")
        );
    }

    public JsonNode obtenerMisInvitaciones(String token) {
        return http.get(
                BASE_PATH + "/mis-invitaciones",
                tokenHeader(token),
                JsonNode.class
        );
    }

    public JsonNode listarBancosTestDisponibles(String token) {
        return http.get(
                BASE_PATH + "/bancos-test-disponibles",
                tokenHeader(token),
                JsonNode.class
        );
    }

    public JsonNode aceptarInvitacion(
            Long idInvitacion,
            String token,
            String usuario,
            String ip
    ) {
        return ejecutarComandoConAuditoria(
                "/{idInvitacion}/aceptar",
                token,
                usuario,
                ip,
                idInvitacion
        );
    }

    public JsonNode rechazarInvitacion(
            Long idInvitacion,
            String token,
            String usuario,
            String ip
    ) {
        return ejecutarComandoConAuditoria(
                "/{idInvitacion}/rechazar",
                token,
                usuario,
                ip,
                idInvitacion
        );
    }

    public JsonNode iniciarTesteo(
            Long idBanco,
            String token,
            String usuario,
            String ip
    ) {
        return ejecutarComandoConAuditoria(
                "/iniciar-testeo/{idBanco}",
                token,
                usuario,
                ip,
                idBanco
        );
    }

    public JsonNode completarTesteo(
            Long idBanco,
            String token,
            String usuario,
            String ip
    ) {
        return ejecutarComandoConAuditoria(
                "/completar-testeo/{idBanco}",
                token,
                usuario,
                ip,
                idBanco
        );
    }

    private JsonNode ejecutarComandoConAuditoria(
            String path,
            String token,
            String usuario,
            String ip,
            Object pathVariable
    ) {
        return http.post(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + path)
                        .queryParam("usuario", usuario)
                        .queryParam("ip", ip)
                        .build(pathVariable),
                null,
                tokenHeader(token),
                JsonNode.class
        );
    }

    private Map<String, String> tokenHeader(String token) {
        return Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}

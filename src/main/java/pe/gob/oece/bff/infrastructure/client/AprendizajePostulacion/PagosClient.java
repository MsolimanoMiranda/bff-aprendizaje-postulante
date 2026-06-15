package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Component
public class PagosClient {

    private static final String PATH_NIUBIZ_CALLBACK = "/api/v1/niubiz/webhook/callback";
    private static final String PATH_TASA            = "/api/v1/pagos/tasa";

    private final DownstreamWebClient http;

    public PagosClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PAG-EX")
        );
    }

    // ── Tasa de pago SICAN ────────────────────────────────────────────────────

    /**
     * Propaga el Bearer token del postulante para que el MS lo reenvíe
     * al servicio de Pagos Electrónicos OECE.
     */
    public JsonNode obtenerTasa(String bearerToken) {
        return http.get(
                PATH_TASA,
                Map.of("Authorization", "Bearer " + bearerToken),
                JsonNode.class
        );
    }

    // ── Niubiz webhooks ───────────────────────────────────────────────────────

    /**
     * GET callback de Niubiz.
     *
     * @param idPostulacion obligatorio — identifica la postulación que generó el pago
     * @param trama         opcional  — trama completa enviada por Niubiz
     */
    public JsonNode callbackNiubiz(
            Long   idPostulacion,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String currency,
            String transactionDate,
            String montoAprobado,
            String trama
    ) {
        return http.get(
                ub -> ub.path(PATH_NIUBIZ_CALLBACK)
                        .queryParam("postulacion",    idPostulacion)
                        .queryParam("purchaseNumber", purchaseNumber)
                        .queryParam("status",         status)
                        .queryParamIfPresent("motivoDenegacion", Optional.ofNullable(motivoDenegacion))
                        .queryParamIfPresent("currency",         Optional.ofNullable(currency))
                        .queryParamIfPresent("transactionDate",  Optional.ofNullable(transactionDate))
                        .queryParamIfPresent("montoAprobado",    Optional.ofNullable(montoAprobado))
                        .queryParamIfPresent("trama",            Optional.ofNullable(trama))
                        .build(),
                null,
                JsonNode.class
        );
    }

    /**
     * POST callback de Niubiz.
     *
     * @param idPostulacion obligatorio — identifica la postulación
     * @param trama         opcional  — trama completa de Niubiz
     */
    public JsonNode callbackNiubizPost(
            Long   idPostulacion,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String transactionDate,
            String montoAprobado,
            String trama
    ) {
        return http.post(
                ub -> ub.path(PATH_NIUBIZ_CALLBACK)
                        .queryParamIfPresent("postulacion",      Optional.of(idPostulacion))
                        .queryParamIfPresent("purchaseNumber",   Optional.ofNullable(purchaseNumber))
                        .queryParamIfPresent("status",           Optional.ofNullable(status))
                        .queryParamIfPresent("motivoDenegacion", Optional.ofNullable(motivoDenegacion))
                        .queryParamIfPresent("transactionDate",  Optional.ofNullable(transactionDate))
                        .queryParamIfPresent("montoAprobado",    Optional.ofNullable(montoAprobado))
                        .queryParamIfPresent("trama",            Optional.ofNullable(trama))
                        .build(),
                null,   // POST sin body — Niubiz envía todo por query params
                null,
                JsonNode.class
        );
    }
}
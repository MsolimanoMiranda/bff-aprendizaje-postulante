package pe.gob.oece.bff.controller;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.config.ApiPaths;

@RestController
@RequestMapping(ApiPaths.CONNECTIVITY)
public class ConnectivityController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectivityController.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient aprendizajePostulacionWebClient;
    private final String postulacionBaseUrl;

    public ConnectivityController(
        @Qualifier("aprendizajePostulacionWebClient") WebClient aprendizajePostulacionWebClient,
        @Value("${app.clients.aprendizaje-postulacion.base-url}") String postulacionBaseUrl

    ) {
        this.aprendizajePostulacionWebClient = aprendizajePostulacionWebClient;
        this.postulacionBaseUrl = postulacionBaseUrl;

    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkConnectivity() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("aprendizaje-postulacion", checkService(aprendizajePostulacionWebClient, postulacionBaseUrl));

        boolean allUp = result.values().stream()
            .map(v -> (Map<?, ?>) v)
            .allMatch(m -> "UP".equals(m.get("status")));

        return allUp
            ? ResponseEntity.ok(result)
            : ResponseEntity.status(503).body(result);
    }

    private Map<String, Object> checkService(WebClient webClient, String baseUrl) {
        long start = System.currentTimeMillis();
        try {
            Map<?, ?> health = webClient.get()
                .uri("/actuator/health")
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(TIMEOUT)
                .block();

            long elapsed = System.currentTimeMillis() - start;
            String remoteStatus = health != null ? String.valueOf(health.get("status")) : "UNKNOWN";

            return Map.of(
                "status", "UP".equals(remoteStatus) ? "UP" : "DOWN",
                "remoteStatus", remoteStatus,
                "url", baseUrl + "/actuator/health",
                "responseTimeMs", elapsed
            );
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - start;
            logger.warn("Connectivity check failed for {}: {}", baseUrl, ex.getMessage());
            return Map.of(
                "status", "DOWN",
                "url", baseUrl + "/actuator/health",
                "error", ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName(),
                "responseTimeMs", elapsed
            );
        }
    }
}

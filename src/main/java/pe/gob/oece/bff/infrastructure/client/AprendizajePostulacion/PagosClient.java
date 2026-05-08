package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.NiubizCallbackRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RespuestaSimple;

import java.time.Duration;
import java.util.Optional;

@Component
public class PagosClient {

    private static final String PATH_NIUBIZ_CALLBACK = "/api/v1/niubiz/webhook/callback";

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

    public RespuestaSimple callbackNiubiz(
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String currency,
            String transactionDate,
            String montoAprobado
    ) {
        return http.get(
                ub -> ub.path(PATH_NIUBIZ_CALLBACK)
                        .queryParam("purchaseNumber", purchaseNumber)
                        .queryParam("status", status)
                        .queryParamIfPresent("motivoDenegacion", Optional.ofNullable(motivoDenegacion))
                        .queryParamIfPresent("currency", Optional.ofNullable(currency))
                        .queryParamIfPresent("transactionDate", Optional.ofNullable(transactionDate))
                        .queryParamIfPresent("montoAprobado", Optional.ofNullable(montoAprobado))
                        .build(),
                null,
                RespuestaSimple.class
        );
    }

    public RespuestaSimple callbackNiubizPost(
            NiubizCallbackRequest body,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String transactionDate,
            String montoAprobado
    ) {
        return http.post(
                ub -> appendCallbackPostQuery(
                        ub.path(PATH_NIUBIZ_CALLBACK),
                        purchaseNumber,
                        status,
                        motivoDenegacion,
                        transactionDate,
                        montoAprobado
                ).build(),
                body,
                null,
                RespuestaSimple.class
        );
    }

    private static UriBuilder appendCallbackPostQuery(
            UriBuilder ub,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String transactionDate,
            String montoAprobado
    ) {
        return ub
                .queryParamIfPresent("purchaseNumber", Optional.ofNullable(purchaseNumber))
                .queryParamIfPresent("status", Optional.ofNullable(status))
                .queryParamIfPresent("motivoDenegacion", Optional.ofNullable(motivoDenegacion))
                .queryParamIfPresent("transactionDate", Optional.ofNullable(transactionDate))
                .queryParamIfPresent("montoAprobado", Optional.ofNullable(montoAprobado));
    }
}

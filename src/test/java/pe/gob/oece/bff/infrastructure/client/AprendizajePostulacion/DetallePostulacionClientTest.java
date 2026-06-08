package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class DetallePostulacionClientTest {

    @Test
    void obtenerDetalle_construye_path_y_envia_authorization() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DetallePostulacionClient client = build(captured, HttpStatus.OK, "{\"idPostulacion\":7}");

        JsonNode response = client.obtenerDetalle(7L, "jwt-token");

        assertThat(response.get("idPostulacion").asLong()).isEqualTo(7L);
        ClientRequest request = captured.get();
        assertThat(request.method()).isEqualTo(HttpMethod.GET);
        assertThat(request.url().getPath()).isEqualTo("/api/v1/postulaciones/7/detalle");
        assertThat(request.headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer jwt-token");
    }

    private DetallePostulacionClient build(AtomicReference<ClientRequest> captured, HttpStatus status, String body) {
        ExchangeFunction exchange = request -> {
            captured.set(request);
            return Mono.just(
                    ClientResponse.create(status)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(body)
                            .build()
            );
        };
        WebClient webClient = WebClient.builder().exchangeFunction(exchange).build();
        return new DetallePostulacionClient(webClient, 5_000L);
    }
}

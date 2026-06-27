package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PostulanteClientTest {

    @Test
    void obtenerDashboard_envia_GET_con_postulante_y_jwt() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PostulanteClient client = build(captured, HttpStatus.OK, "{\"ok\":true}");

        JsonNode result = client.obtenerDashboard(42L, "jwt-token");

        assertThat(result.get("ok").asBoolean()).isTrue();
        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulante/dashboard");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("42");
        assertThat(req.headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer jwt-token");
    }

    @Test
    void obtenerPerfilWizard_envia_GET_con_postulante_y_jwt() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PostulanteClient client = build(captured, HttpStatus.OK, "{\"ok\":true}");

        client.obtenerPerfilWizard(42L, "jwt-token");

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulante/perfil");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("42");
        assertThat(req.headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer jwt-token");
    }

    private PostulanteClient build(AtomicReference<ClientRequest> captured, HttpStatus status, String body) {
        ExchangeFunction exchange = req -> {
            captured.set(req);
            return Mono.just(
                    ClientResponse.create(status)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(body)
                            .build()
            );
        };
        WebClient webClient = WebClient.builder().exchangeFunction(exchange).build();
        return new PostulanteClient(webClient, 5_000L);
    }
}

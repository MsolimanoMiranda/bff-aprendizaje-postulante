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
import pe.gob.oece.bff.domain.NotFoundException;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostulacionClientTest {

    @Test
    void iniciarPostulacion_envia_POST_y_header_postulante() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PostulacionClient client = build(captured, HttpStatus.CREATED, "{\"id\":1}");

        JsonNode result = client.iniciarPostulacion(
                42L,
                new IniciarPostulacionRequest(true, "2026-05-08T00:00:00")
        );

        assertThat(result.get("id").asInt()).isEqualTo(1);
        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.POST);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("42");
    }

    @Test
    void obtenerDetalle_construye_path_con_id() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PostulacionClient client = build(captured, HttpStatus.OK, "{\"id\":7}");

        client.obtenerDetalle(7L, 42L);

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones/7");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("42");
    }

    @Test
    void eliminarExperiencia_envia_DELETE_con_path_compuesto() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PostulacionClient client = build(captured, HttpStatus.OK, "{}");

        client.eliminarExperiencia(7L, 99L, 42L);

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.DELETE);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones/7/experiencias/99");
    }

    @Test
    void obtenerDetalle_404_propaga_NotFoundException() {
        PostulacionClient client = build(new AtomicReference<>(), HttpStatus.NOT_FOUND, "{}");

        assertThatThrownBy(() -> client.obtenerDetalle(999L, 42L))
                .isInstanceOf(NotFoundException.class);
    }

    private PostulacionClient build(AtomicReference<ClientRequest> captured, HttpStatus status, String body) {
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
        return new PostulacionClient(webClient, 5_000L);
    }
}

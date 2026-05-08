package pe.gob.oece.bff.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.domain.DomainException;
import pe.gob.oece.bff.domain.NotFoundException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DownstreamWebClientTest {

    private final DownstreamClientErrorHandler errorHandler =
            new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PL-EX");

    @Test
    void get_envia_uri_con_path_vars_y_headers_y_decodifica_class() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "{\"data\":\"ok\"}");

        JsonNode response = http.get(
                "/api/v1/postulaciones/{id}",
                Map.of("X-Postulante-Id", "42"),
                JsonNode.class,
                7L
        );

        assertThat(response.get("data").asText()).isEqualTo("ok");

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones/7");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("42");
    }

    @Test
    void get_con_parameterizedType_devuelve_lista() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "[1,2,3]");

        List<Integer> response = http.get(
                "/api/v1/numeros",
                null,
                new ParameterizedTypeReference<List<Integer>>() {}
        );

        assertThat(response).containsExactly(1, 2, 3);
    }

    @Test
    void get_con_uriFunction_arma_query_params() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "{}");

        http.get(
                ub -> ub.path("/api/v1/publico/programaciones")
                        .queryParam("idDepartamento", 15)
                        .queryParam("pagina", 0)
                        .build(),
                null,
                JsonNode.class
        );

        URI url = captured.get().url();
        assertThat(url.getPath()).isEqualTo("/api/v1/publico/programaciones");
        assertThat(url.getQuery()).contains("idDepartamento=15", "pagina=0");
    }

    @Test
    void post_envia_verbo_path_vars_y_headers() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.CREATED, "{\"id\":99}");

        Map<String, Object> body = Map.of("aceptaTerminos", true);
        JsonNode response = http.post(
                "/api/v1/postulaciones",
                body,
                Map.of("X-Postulante-Id", "5"),
                JsonNode.class
        );

        assertThat(response.get("id").asInt()).isEqualTo(99);
        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.POST);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones");
        assertThat(req.headers().getFirst("X-Postulante-Id")).isEqualTo("5");

    }

    @Test
    void post_con_body_null_no_setea_content_type_de_json() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "{}");

        http.post("/api/v1/test", null, null, JsonNode.class);

        assertThat(captured.get().headers().getContentType()).isNull();
    }

    @Test
    void put_actualiza_recurso() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "{}");

        http.put(
                "/api/v1/postulaciones/{id}/experiencias/{idExp}",
                Map.of("entidad", "X"),
                Map.of("X-Postulante-Id", "5"),
                JsonNode.class,
                10L,
                20L
        );

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.PUT);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones/10/experiencias/20");
    }

    @Test
    void delete_elimina_recurso() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        DownstreamWebClient http = build(captured, HttpStatus.OK, "{}");

        http.delete(
                "/api/v1/postulaciones/{id}/experiencias/{idExp}",
                Map.of("X-Postulante-Id", "5"),
                JsonNode.class,
                10L,
                20L
        );

        ClientRequest req = captured.get();
        assertThat(req.method()).isEqualTo(HttpMethod.DELETE);
        assertThat(req.url().getPath()).isEqualTo("/api/v1/postulaciones/10/experiencias/20");
    }

    @Test
    void get_404_propaga_NotFoundException() {
        DownstreamWebClient http = build(new AtomicReference<>(), HttpStatus.NOT_FOUND, "{\"detail\":\"x\"}");

        assertThatThrownBy(() -> http.get("/api/v1/postulaciones/999", null, JsonNode.class))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void post_422_propaga_DomainException() {
        DownstreamWebClient http = build(new AtomicReference<>(), HttpStatus.UNPROCESSABLE_ENTITY, "{\"message\":\"invalido\"}");

        assertThatThrownBy(() -> http.post("/api/v1/postulaciones", Map.of(), null, JsonNode.class))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void timeout_respeta_duracion() {
        WebClient slow = WebClient.builder()
                .exchangeFunction(req -> Mono.never())
                .build();
        DownstreamWebClient http = new DownstreamWebClient(slow, Duration.ofMillis(50), errorHandler);

        assertThatThrownBy(() -> http.get("/api/v1/x", null, JsonNode.class))
                .hasRootCauseInstanceOf(java.util.concurrent.TimeoutException.class);
    }

    private DownstreamWebClient build(AtomicReference<ClientRequest> captured, HttpStatus status, String body) {
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
        return new DownstreamWebClient(webClient, Duration.ofSeconds(5), errorHandler);
    }
}

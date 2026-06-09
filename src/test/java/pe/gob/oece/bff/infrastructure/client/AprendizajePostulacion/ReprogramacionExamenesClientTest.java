package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ReprogramacionExamenesClientTest {

    @Test
    void buscarProgramacionesDisponibles_arma_query_params() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        ReprogramacionExamenesClient client = build(captured, HttpStatus.OK, "{}");

        client.buscarProgramacionesDisponibles(
                new BusquedaProgramacionDisponibleRequest(
                        1L, "2026-06-01", "2026-06-30", "FECHA_EXAMEN", "ASC", 1, 10
                ),
                "Bearer token"
        );

        URI url = captured.get().url();
        assertThat(captured.get().method()).isEqualTo(HttpMethod.GET);
        assertThat(captured.get().headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer token");
        assertThat(url.getPath()).isEqualTo("/api/reprogramaciones/programaciones-disponibles");
        assertThat(url.getQuery())
                .contains("idLocal=1", "fechaDesde=2026-06-01", "fechaHasta=2026-06-30",
                        "columnaOrden=FECHA_EXAMEN", "direccionOrden=ASC", "pagina=1", "tamano=10");
    }

    @Test
    void registrarReprogramacion_envia_post_al_ms() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        ReprogramacionExamenesClient client = build(captured, HttpStatus.OK, "{}");

        client.registrarReprogramacion(new RegistrarReprogramacionRequest(10L, 30L), "Bearer token");

        assertThat(captured.get().method()).isEqualTo(HttpMethod.POST);
        assertThat(captured.get().headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer token");
        assertThat(captured.get().url().getPath()).isEqualTo("/api/reprogramaciones/registrar");
    }

    private ReprogramacionExamenesClient build(
            AtomicReference<ClientRequest> captured,
            HttpStatus status,
            String body
    ) {
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
        return new ReprogramacionExamenesClient(webClient, 5_000L);
    }
}

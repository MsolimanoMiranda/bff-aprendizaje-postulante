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
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaProgramacionRequest;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PublicoClientTest {

    @Test
    void buscarProgramaciones_arma_query_params_y_omite_nulls() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PublicoClient client = build(captured, HttpStatus.OK, "{}");

        client.buscarProgramaciones(new BusquedaProgramacionRequest(
                15L, null, null, "FECHA_EXAMEN", "ASC", 0, 20
        ));

        URI url = captured.get().url();
        assertThat(captured.get().method()).isEqualTo(HttpMethod.GET);
        assertThat(url.getPath()).isEqualTo("/api/v1/publico/programaciones");
        assertThat(url.getQuery())
                .contains("idDepartamento=15", "columnaOrden=FECHA_EXAMEN", "direccionOrden=ASC", "pagina=0", "tamano=20")
                .doesNotContain("idLocal", "direccion=");
    }

    @Test
    void buscarCertificados_omite_strings_vacios() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PublicoClient client = build(captured, HttpStatus.OK, "{}");

        client.buscarCertificados(new BusquedaCertificadoRequest(
                "POR_DOCUMENTO", "", "DNI", "12345678", "", "", 0, 10
        ));

        URI url = captured.get().url();
        assertThat(url.getQuery())
                .contains("criterio=POR_DOCUMENTO", "tipoDocumento=DNI", "nroDocumento=12345678")
                .doesNotContain("nroCertificado=", "nombres=", "apellidos=");
    }

    @Test
    void obtenerDetalleCertificado_construye_path_con_id() {
        AtomicReference<ClientRequest> captured = new AtomicReference<>();
        PublicoClient client = build(captured, HttpStatus.OK, "{}");

        client.obtenerDetalleCertificado(123L);

        assertThat(captured.get().url().getPath()).isEqualTo("/api/v1/publico/certificados/123");
    }

    private PublicoClient build(AtomicReference<ClientRequest> captured, HttpStatus status, String body) {
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
        return new PublicoClient(webClient, 5_000L);
    }
}

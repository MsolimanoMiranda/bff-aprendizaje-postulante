package pe.gob.oece.bff.infrastructure.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.gob.oece.bff.domain.DomainException;
import pe.gob.oece.bff.domain.NotFoundException;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class DownstreamClientErrorHandlerTest {

    private final DownstreamClientErrorHandler handler =
            new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PL-EX");

    @Test
    void mapError_status404_devuelveNotFoundException() {
        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(404), "{\"detail\":\"no existe\"}");

        assertThat(ex).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("aprendizaje-postulacion");
    }

    @Test
    void mapError_status400_devuelveDomainExceptionConPrefijoYBody() {
        String body = "{\"message\":\"input invalido\"}";

        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(400), body);

        assertThat(ex).isInstanceOf(DomainException.class).hasMessage(body);
        assertThat(((DomainException) ex).getCode()).isEqualTo("ERROR-AP-PL-EX400");
    }

    @Test
    void mapError_status409_devuelveDomainException() {
        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(409), "conflicto");

        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(((DomainException) ex).getCode()).isEqualTo("ERROR-AP-PL-EX409");
    }

    @Test
    void mapError_status422_devuelveDomainException() {
        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(422), "no procesable");

        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(((DomainException) ex).getCode()).isEqualTo("ERROR-AP-PL-EX422");
    }

    @Test
    void mapError_status500_devuelveWebClientResponseException() {
        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(500), "boom");

        assertThat(ex).isInstanceOf(WebClientResponseException.class);
        WebClientResponseException wcre = (WebClientResponseException) ex;
        assertThat(wcre.getStatusCode().value()).isEqualTo(500);
        assertThat(wcre.getResponseBodyAsString()).isEqualTo("boom");
    }

    @Test
    void mapError_status401_devuelveWebClientResponseException() {
        RuntimeException ex = handler.mapError(HttpStatusCode.valueOf(401), null);

        assertThat(ex).isInstanceOf(WebClientResponseException.class);
        assertThat(((WebClientResponseException) ex).getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void toError_emite_la_excepcion_mapeada() {
        ClientResponse response = ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body("{\"detail\":\"missing\"}")
                .build();

        StepVerifier.create(handler.toError(response))
                .expectNextMatches(t -> t instanceof NotFoundException)
                .verifyComplete();
    }
}

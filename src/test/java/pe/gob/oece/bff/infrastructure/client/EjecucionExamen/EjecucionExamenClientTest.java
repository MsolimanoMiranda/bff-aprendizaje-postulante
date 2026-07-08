package pe.gob.oece.bff.infrastructure.client.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EjecucionExamenClientTest {

    private static final String BASE_PATH = "/api/v1/ejecucion-examen";

    private EjecucionExamenClient client;
    private DownstreamWebClient http;
    private JsonNode response;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost")
                .build();

        client = new EjecucionExamenClient(webClient, 8000);

        http = mock(DownstreamWebClient.class);
        response = mock(JsonNode.class);

        ReflectionTestUtils.setField(client, "http", http);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void validarToken_debeHacerPostAlEndpointCorrecto() {
        ValidarTokenExamenRequest request = mock(ValidarTokenExamenRequest.class);

        when(http.post(eq(BASE_PATH + "/validar-token"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.validarToken(request);

        assertSame(response, result);
        verify(http).post(eq(BASE_PATH + "/validar-token"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void validarToken_debeReenviarAuthorizationDelRequestActual() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));
        ValidarTokenExamenRequest request = mock(ValidarTokenExamenRequest.class);

        when(http.post(eq(BASE_PATH + "/validar-token"), same(request), anyMap(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.validarToken(request);

        assertSame(response, result);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> headersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(http).post(eq(BASE_PATH + "/validar-token"), same(request), headersCaptor.capture(), eq(JsonNode.class));
        assertEquals("Bearer token", headersCaptor.getValue().get(HttpHeaders.AUTHORIZATION));
    }


    @Test
    void iniciar_debeHacerPostAlEndpointCorrecto() {
        IniciarEjecucionExamenRequest request = mock(IniciarEjecucionExamenRequest.class);

        when(http.post(eq(BASE_PATH + "/iniciar"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.iniciar(request);

        assertSame(response, result);
        verify(http).post(eq(BASE_PATH + "/iniciar"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void heartbeat_debeHacerPostAlEndpointCorrecto() {
        HeartbeatEjecucionExamenRequest request = mock(HeartbeatEjecucionExamenRequest.class);

        when(http.post(eq(BASE_PATH + "/heartbeat"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.heartbeat(request);

        assertSame(response, result);
        verify(http).post(eq(BASE_PATH + "/heartbeat"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void guardarRespuestas_debeHacerPostAlEndpointCorrecto() {
        GuardarRespuestasRequest request = mock(GuardarRespuestasRequest.class);

        when(http.post(eq(BASE_PATH + "/examen/guardar-respuestas"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.guardarRespuestas(request);

        assertSame(response, result);
        verify(http).post(eq(BASE_PATH + "/examen/guardar-respuestas"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void listarCursosRecomendados_debeHacerPostAlEndpointCorrecto() {
        List<Long> idsCompetencia = List.of(1L, 2L, 3L);

        when(http.post(eq(BASE_PATH + "/examen/cursos-recomendados"), same(idsCompetencia), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.listarCursosRecomendados(idsCompetencia);

        assertSame(response, result);
        verify(http).post(eq(BASE_PATH + "/examen/cursos-recomendados"), same(idsCompetencia), isNull(), eq(JsonNode.class));
    }

    @Test
    void actualizarPreguntaActual_debeHacerPutAlEndpointCorrecto() {
        ActualizarPreguntaActualRequest request = mock(ActualizarPreguntaActualRequest.class);

        when(http.put(eq(BASE_PATH + "/pregunta-actual"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.actualizarPreguntaActual(request);

        assertSame(response, result);
        verify(http).put(eq(BASE_PATH + "/pregunta-actual"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void cambiarEstado_debeHacerPutAlEndpointCorrecto() {
        CambiarEstadoEjecucionRequest request = mock(CambiarEstadoEjecucionRequest.class);

        when(http.put(eq(BASE_PATH + "/estado"), same(request), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.cambiarEstado(request);

        assertSame(response, result);
        verify(http).put(eq(BASE_PATH + "/estado"), same(request), isNull(), eq(JsonNode.class));
    }

    @Test
    void obtenerPorExamen_debeHacerGetConPathVariable() {
        Integer idExamen = 10;

        when(http.get(eq(BASE_PATH + "/{idExamen}"), isNull(), eq(JsonNode.class), eq(idExamen)))
                .thenReturn(response);

        JsonNode result = client.obtenerPorExamen(idExamen);

        assertSame(response, result);
        verify(http).get(eq(BASE_PATH + "/{idExamen}"), isNull(), eq(JsonNode.class), eq(idExamen));
    }

    @Test
    void obtenerEstadoIndividual_debeHacerGetConQueryParamIdInscripcion() {
        Long idInscripcion = 99L;

        when(http.get(anyUriFunction(), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.obtenerEstadoIndividual(idInscripcion);

        assertSame(response, result);
        assertBuiltUriEquals(BASE_PATH + "/examen/status?idInscripcion=99");
    }

    @Test
    void obtenerResultados_debeHacerGetConQueryParamIdExamen() {
        Long idExamen = 25L;

        when(http.get(anyUriFunction(), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.obtenerResultados(idExamen);

        assertSame(response, result);
        assertBuiltUriEquals(BASE_PATH + "/examen/resultado?idExamen=25");
    }

    @Test
    void obtenerInformacionExamen_debeHacerGetConQueryParamIdInscripcion() {
        Long idInscripcion = 88L;

        when(http.get(anyUriFunction(), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.obtenerInformacionExamen(idInscripcion);

        assertSame(response, result);
        assertBuiltUriEquals(BASE_PATH + "/examen/informacion?idInscripcion=88");
    }

    @Test
    void finalizar_debeHacerGetConQueryParamIdExamen() {
        Long idExamen = 77L;

        when(http.get(anyUriFunction(), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.finalizar(idExamen);

        assertSame(response, result);
        assertBuiltUriEquals(BASE_PATH + "/examen/finalizar?idExamen=77");
    }

    @Test
    void finalizar_debeReenviarAuthorizationExplicito() {
        Long idExamen = 77L;

        when(http.get(anyUriFunction(), anyMap(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.finalizar(idExamen, "Bearer token");

        assertSame(response, result);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> headersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(http).get(anyUriFunction(), headersCaptor.capture(), eq(JsonNode.class));
        assertEquals("Bearer token", headersCaptor.getValue().get(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void obtenerPreguntasRespondidas_debeHacerGetConQueryParamIdInscripcion() {
        Long idInscripcion = 66L;

        when(http.get(anyUriFunction(), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        JsonNode result = client.obtenerPreguntasRespondidas(idInscripcion);

        assertSame(response, result);
        assertBuiltUriEquals(BASE_PATH + "/examen/Obtener?idInscripcion=66");
    }

    @SuppressWarnings("unchecked")
    private Function<UriBuilder, URI> anyUriFunction() {
        return any(Function.class);
    }

    @SuppressWarnings("unchecked")
    private void assertBuiltUriEquals(String expectedUri) {
        ArgumentCaptor<Function<UriBuilder, URI>> captor = ArgumentCaptor.forClass(Function.class);

        verify(http).get(captor.capture(), isNull(), eq(JsonNode.class));

        URI uri = captor.getValue()
                .apply(new DefaultUriBuilderFactory().builder());

        assertEquals(expectedUri, uri.toString());
    }
}

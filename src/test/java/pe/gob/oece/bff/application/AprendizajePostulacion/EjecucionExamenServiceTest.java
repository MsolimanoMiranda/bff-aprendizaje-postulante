package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.EjecucionExamenClient;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EjecucionExamenServiceTest {

    private static final String IP_ORIGEN = "192.168.1.10";
    private static final String AUTHORIZATION = "Bearer token";

    @Mock
    private EjecucionExamenClient ejecucionExamenClient;

    @Mock
    private JsonNode response;

    private EjecucionExamenService service;

    @BeforeEach
    void setUp() {
        service = new EjecucionExamenService(ejecucionExamenClient);
    }

    @Test
    void validarToken_debeDelegarAlClientYRetornarRespuesta() {
        ValidarTokenExamenRequest request = mock(ValidarTokenExamenRequest.class);

        when(request.idInscripcion()).thenReturn(100L);
        when(ejecucionExamenClient.validarToken(same(request))).thenReturn(response);

        JsonNode result = service.validarToken(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).validarToken(same(request));
    }

    @Test
    void iniciar_debeDelegarAlClientYRetornarRespuesta() {
        IniciarEjecucionExamenRequest request = mock(IniciarEjecucionExamenRequest.class);

        when(request.idExamen()).thenReturn(10);
        when(ejecucionExamenClient.iniciar(same(request))).thenReturn(response);

        JsonNode result = service.iniciar(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).iniciar(same(request));
    }

    @Test
    void heartbeat_debeDelegarAlClientYRetornarRespuesta() {
        HeartbeatEjecucionExamenRequest request = mock(HeartbeatEjecucionExamenRequest.class);

        when(request.idEjecucion()).thenReturn(20);
        when(ejecucionExamenClient.heartbeat(same(request))).thenReturn(response);

        JsonNode result = service.heartbeat(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).heartbeat(same(request));
    }

    @Test
    void guardarRespuestas_debeDelegarAlClientYRetornarRespuesta() {
        GuardarRespuestasRequest request = mock(GuardarRespuestasRequest.class);

        when(request.idExamen()).thenReturn(30L);
        when(ejecucionExamenClient.guardarRespuestas(same(request))).thenReturn(response);

        JsonNode result = service.guardarRespuestas(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).guardarRespuestas(same(request));
    }

    @Test
    void listarCursosRecomendados_debeDelegarAlClientYRetornarRespuesta() {
        List<Long> idsCompetencia = List.of(1L, 2L, 3L);

        when(ejecucionExamenClient.listarCursosRecomendados(same(idsCompetencia))).thenReturn(response);

        JsonNode result = service.listarCursosRecomendados(idsCompetencia, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).listarCursosRecomendados(same(idsCompetencia));
    }

    @Test
    void listarCursosRecomendados_conListaNull_debeDelegarAlClientYRetornarRespuesta() {
        when(ejecucionExamenClient.listarCursosRecomendados(null)).thenReturn(response);

        JsonNode result = service.listarCursosRecomendados(null, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).listarCursosRecomendados(null);
    }

    @Test
    void actualizarPreguntaActual_debeDelegarAlClientYRetornarRespuesta() {
        ActualizarPreguntaActualRequest request = mock(ActualizarPreguntaActualRequest.class);

        when(request.idEjecucion()).thenReturn(40);
        when(request.idPregunta()).thenReturn(50);
        when(ejecucionExamenClient.actualizarPreguntaActual(same(request))).thenReturn(response);

        JsonNode result = service.actualizarPreguntaActual(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).actualizarPreguntaActual(same(request));
    }

    @Test
    void cambiarEstado_debeDelegarAlClientYRetornarRespuesta() {
        CambiarEstadoEjecucionRequest request = mock(CambiarEstadoEjecucionRequest.class);

        when(request.idEjecucion()).thenReturn(60);
        when(request.nuevoEstado()).thenReturn("FINALIZADO");
        when(ejecucionExamenClient.cambiarEstado(same(request))).thenReturn(response);

        JsonNode result = service.cambiarEstado(request, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).cambiarEstado(same(request));
    }

    @Test
    void obtenerPorExamen_debeDelegarAlClientYRetornarRespuesta() {
        Integer idExamen = 70;

        when(ejecucionExamenClient.obtenerPorExamen(idExamen)).thenReturn(response);

        JsonNode result = service.obtenerPorExamen(idExamen, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).obtenerPorExamen(idExamen);
    }

    @Test
    void obtenerEstadoIndividual_debeDelegarAlClientYRetornarRespuesta() {
        Long idInscripcion = 80L;

        when(ejecucionExamenClient.obtenerEstadoIndividual(idInscripcion)).thenReturn(response);

        JsonNode result = service.obtenerEstadoIndividual(idInscripcion, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).obtenerEstadoIndividual(idInscripcion);
    }

    @Test
    void obtenerResultados_debeDelegarAlClientYRetornarRespuesta() {
        Long idExamen = 90L;

        when(ejecucionExamenClient.obtenerResultados(idExamen)).thenReturn(response);

        JsonNode result = service.obtenerResultados(idExamen, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).obtenerResultados(idExamen);
    }

    @Test
    void obtenerInformacionExamen_debeDelegarAlClientYRetornarRespuesta() {
        Long idInscripcion = 100L;

        when(ejecucionExamenClient.obtenerInformacionExamen(idInscripcion)).thenReturn(response);

        JsonNode result = service.obtenerInformacionExamen(idInscripcion, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).obtenerInformacionExamen(idInscripcion);
    }

    @Test
    void finalizar_debeDelegarAlClientYRetornarRespuesta() {
        Long idExamen = 110L;

        when(ejecucionExamenClient.finalizar(idExamen, AUTHORIZATION)).thenReturn(response);

        JsonNode result = service.finalizar(idExamen, IP_ORIGEN, AUTHORIZATION);

        assertSame(response, result);
        verify(ejecucionExamenClient).finalizar(idExamen, AUTHORIZATION);
    }

    @Test
    void obtenerPreguntasRespondidas_debeDelegarAlClientYRetornarRespuesta() {
        Long idInscripcion = 120L;

        when(ejecucionExamenClient.obtenerPreguntasRespondidas(idInscripcion)).thenReturn(response);

        JsonNode result = service.obtenerPreguntasRespondidas(idInscripcion, IP_ORIGEN);

        assertSame(response, result);
        verify(ejecucionExamenClient).obtenerPreguntasRespondidas(idInscripcion);
    }
}

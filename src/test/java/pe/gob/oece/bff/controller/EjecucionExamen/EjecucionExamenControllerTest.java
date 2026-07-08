package pe.gob.oece.bff.controller.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.application.AprendizajePostulacion.EjecucionExamenService;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;
import pe.gob.oece.bff.shared.ApiWrapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EjecucionExamenControllerTest {

    private static final String IP_ORIGEN = "192.168.1.10";
    private static final String AUTHORIZATION = "Bearer token";

    @Mock
    private EjecucionExamenService ejecucionExamenService;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private JsonNode data;

    private EjecucionExamenController controller;

    @BeforeEach
    void setUp() {
        controller = new EjecucionExamenController(ejecucionExamenService);

        when(httpRequest.getRemoteAddr()).thenReturn(IP_ORIGEN);
        when(httpRequest.getHeader(anyString())).thenReturn(null);
    }

    @Test
    void validarToken_debeDelegarAlServiceYRetornarApiWrapper() {
        ValidarTokenExamenRequest request = mock(ValidarTokenExamenRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/validar-token");
        when(ejecucionExamenService.validarToken(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.validarToken(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).validarToken(same(request), eq(IP_ORIGEN));
    }

    @Test
    void iniciar_debeDelegarAlServiceYRetornarApiWrapper() {
        IniciarEjecucionExamenRequest request = mock(IniciarEjecucionExamenRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/iniciar");
        when(ejecucionExamenService.iniciar(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.iniciar(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).iniciar(same(request), eq(IP_ORIGEN));
    }

    @Test
    void heartbeat_debeDelegarAlServiceYRetornarApiWrapper() {
        HeartbeatEjecucionExamenRequest request = mock(HeartbeatEjecucionExamenRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/heartbeat");
        when(ejecucionExamenService.heartbeat(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.heartbeat(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).heartbeat(same(request), eq(IP_ORIGEN));
    }

    @Test
    void actualizarPreguntaActual_debeDelegarAlServiceYRetornarApiWrapper() {
        ActualizarPreguntaActualRequest request = mock(ActualizarPreguntaActualRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/pregunta-actual");
        when(ejecucionExamenService.actualizarPreguntaActual(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.actualizarPreguntaActual(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).actualizarPreguntaActual(same(request), eq(IP_ORIGEN));
    }

    @Test
    void cambiarEstado_debeDelegarAlServiceYRetornarApiWrapper() {
        CambiarEstadoEjecucionRequest request = mock(CambiarEstadoEjecucionRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/estado");
        when(ejecucionExamenService.cambiarEstado(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.cambiarEstado(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).cambiarEstado(same(request), eq(IP_ORIGEN));
    }

    @Test
    void obtenerPorExamen_debeDelegarAlServiceYRetornarApiWrapper() {
        Integer idExamen = 10;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/10");
        when(ejecucionExamenService.obtenerPorExamen(eq(idExamen), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.obtenerPorExamen(idExamen, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).obtenerPorExamen(eq(idExamen), eq(IP_ORIGEN));
    }

    @Test
    void obtenerEstadoIndividual_debeDelegarAlServiceYRetornarApiWrapper() {
        Long idInscripcion = 99L;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/status");
        when(ejecucionExamenService.obtenerEstadoIndividual(eq(idInscripcion), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.obtenerEstadoIndividual(idInscripcion, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).obtenerEstadoIndividual(eq(idInscripcion), eq(IP_ORIGEN));
    }

    @Test
    void obtenerResultados_debeDelegarAlServiceYRetornarApiWrapper() {
        Long idExamen = 20L;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/resultado");
        when(ejecucionExamenService.obtenerResultados(eq(idExamen), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.obtenerResultados(idExamen, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).obtenerResultados(eq(idExamen), eq(IP_ORIGEN));
    }

    @Test
    void obtenerInformacionExamen_debeDelegarAlServiceYRetornarApiWrapper() {
        Long idInscripcion = 30L;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/informacion");
        when(ejecucionExamenService.obtenerInformacionExamen(eq(idInscripcion), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.obtenerInformacionExamen(idInscripcion, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).obtenerInformacionExamen(eq(idInscripcion), eq(IP_ORIGEN));
    }

    @Test
    void finalizar_debeDelegarAlServiceYRetornarApiWrapper() {
        Long idExamen = 40L;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/finalizar");
        when(ejecucionExamenService.finalizar(eq(idExamen), eq(IP_ORIGEN), eq(AUTHORIZATION))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.finalizar(idExamen, AUTHORIZATION, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).finalizar(eq(idExamen), eq(IP_ORIGEN), eq(AUTHORIZATION));
    }

    @Test
    void obtenerPreguntasRespondidas_debeDelegarAlServiceYRetornarApiWrapper() {
        Long idInscripcion = 50L;
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/Obtener");
        when(ejecucionExamenService.obtenerPreguntasRespondidas(eq(idInscripcion), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.obtenerPreguntasRespondidas(idInscripcion, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).obtenerPreguntasRespondidas(eq(idInscripcion), eq(IP_ORIGEN));
    }

    @Test
    void guardarRespuestas_debeDelegarAlServiceYRetornarApiWrapper() {
        GuardarRespuestasRequest request = mock(GuardarRespuestasRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/guardar-respuestas");
        when(ejecucionExamenService.guardarRespuestas(same(request), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.guardarRespuestas(request, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).guardarRespuestas(same(request), eq(IP_ORIGEN));
    }

    @Test
    void listarCursosRecomendados_debeDelegarAlServiceYRetornarApiWrapper() {
        List<Long> idsCompetencia = List.of(1L, 2L, 3L);
        when(httpRequest.getRequestURI()).thenReturn("/ejecucion-examen/examen/cursos-recomendados");
        when(ejecucionExamenService.listarCursosRecomendados(same(idsCompetencia), eq(IP_ORIGEN))).thenReturn(data);

        ApiWrapper<JsonNode> result = controller.listarCursosRecomendados(idsCompetencia, httpRequest);

        assertNotNull(result);
        verify(ejecucionExamenService).listarCursosRecomendados(same(idsCompetencia), eq(IP_ORIGEN));
    }
}

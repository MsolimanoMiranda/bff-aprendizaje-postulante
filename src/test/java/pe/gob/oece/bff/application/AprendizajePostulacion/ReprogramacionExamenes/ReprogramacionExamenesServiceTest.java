package pe.gob.oece.bff.application.AprendizajePostulacion.ReprogramacionExamenes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.ReprogramacionExamenesClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReprogramacionExamenesServiceTest {

    @Mock
    private ReprogramacionExamenesClient client;

    @InjectMocks
    private ReprogramacionExamenesService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void buscarProgramacionesDisponibles_delega_en_client() {
        var request = new BusquedaProgramacionDisponibleRequest(1L, "2026-06-01", "2026-06-30",
                null, null, 1, 10);
        var response = objectMapper.createObjectNode().put("status", true);
        when(client.buscarProgramacionesDisponibles(request, "Bearer token")).thenReturn(response);

        var result = service.buscarProgramacionesDisponibles(request, "Bearer token", "127.0.0.1");

        assertThat(result).isSameAs(response);
        verify(client).buscarProgramacionesDisponibles(request, "Bearer token");
    }

    @Test
    void registrarReprogramacion_delega_en_client() {
        var request = new RegistrarReprogramacionRequest(100L, 30L);
        var response = objectMapper.createObjectNode().put("rpta", 1);
        when(client.registrarReprogramacion(request, "Bearer token")).thenReturn(response);

        var result = service.registrarReprogramacion(request, "Bearer token", "127.0.0.1");

        assertThat(result).isSameAs(response);
        verify(client).registrarReprogramacion(eq(request), eq("Bearer token"));
    }
}

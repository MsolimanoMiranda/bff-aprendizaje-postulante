package pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.EncuestaSatisfaccionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaDetalleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaRequest;

@ExtendWith(MockitoExtension.class)
class EncuestaSatisfaccionServiceTest {

    private static final String IP_ORIGEN = "203.0.113.8";

    @Mock
    EncuestaSatisfaccionClient encuestaSatisfaccionClient;

    @InjectMocks
    EncuestaSatisfaccionQueryService queryService;

    @InjectMocks
    EncuestaSatisfaccionCommandService commandService;

    private final JsonNode stub = new ObjectMapper().createObjectNode().put("ok", true);

    @Test
    void obtenerFormularioActivo_delega_al_client() {
        when(encuestaSatisfaccionClient.obtenerFormularioActivo()).thenReturn(stub);

        assertThat(queryService.obtenerFormularioActivo(IP_ORIGEN)).isSameAs(stub);

        verify(encuestaSatisfaccionClient).obtenerFormularioActivo();
    }

    @Test
    void registrarRespuesta_envia_idUsuarioGu_al_client() {
        EncuestaRespuestaRequest request = new EncuestaRespuestaRequest(
                1L,
                List.of(new EncuestaRespuestaDetalleRequest(21L, List.of("Masculino")))
        );
        when(encuestaSatisfaccionClient.registrarRespuesta(34082985L, request)).thenReturn(stub);

        assertThat(commandService.registrarRespuesta(34082985L, request, IP_ORIGEN)).isSameAs(stub);

        verify(encuestaSatisfaccionClient).registrarRespuesta(34082985L, request);
    }
}

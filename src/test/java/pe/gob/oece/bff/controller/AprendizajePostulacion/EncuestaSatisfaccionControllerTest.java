package pe.gob.oece.bff.controller.AprendizajePostulacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;
import pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion.EncuestaSatisfaccionCommandService;
import pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion.EncuestaSatisfaccionQueryService;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaDetalleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaRequest;

@ExtendWith(MockitoExtension.class)
class EncuestaSatisfaccionControllerTest {

    @Mock
    EncuestaSatisfaccionQueryService queryService;

    @Mock
    EncuestaSatisfaccionCommandService commandService;

    private final JsonNode stub = new ObjectMapper().createObjectNode().put("registrado", true);

    @Test
    void registrarRespuesta_extrae_idUsuario_del_token_como_idUsuarioGu() {
        EncuestaSatisfaccionController controller = new EncuestaSatisfaccionController(queryService, commandService);
        EncuestaRespuestaRequest request = new EncuestaRespuestaRequest(
                1L,
                List.of(new EncuestaRespuestaDetalleRequest(21L, List.of("Masculino")))
        );
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/encuesta/respuestas");
        when(commandService.registrarRespuesta(34082985L, request, "127.0.0.1")).thenReturn(stub);

        var response = controller.registrarRespuesta(jwtConIdUsuario("34082985"), request, httpRequest);

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getData()).isSameAs(stub);
        verify(commandService).registrarRespuesta(34082985L, request, "127.0.0.1");
    }

    @Test
    void registrarRespuesta_sin_claim_idUsuario_responde_unauthorized() {
        EncuestaSatisfaccionController controller = new EncuestaSatisfaccionController(queryService, commandService);
        EncuestaRespuestaRequest request = new EncuestaRespuestaRequest(
                1L,
                List.of(new EncuestaRespuestaDetalleRequest(21L, List.of("Masculino")))
        );

        assertThatThrownBy(() -> controller.registrarRespuesta(jwtSinIdUsuario(), request, new MockHttpServletRequest()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Token sin claim idUsuario");
    }

    @Test
    void obtenerFormularioActivo_devuelve_wrapper_success() {
        EncuestaSatisfaccionController controller = new EncuestaSatisfaccionController(queryService, commandService);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/encuesta/formulario-activo");
        when(queryService.obtenerFormularioActivo(any())).thenReturn(stub);

        var response = controller.obtenerFormularioActivo(request);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isSameAs(stub);
        verify(queryService).obtenerFormularioActivo("127.0.0.1");
    }

    private Jwt jwtConIdUsuario(String idUsuario) {
        return jwt(Map.of("sub", "dev-user", "idUsuario", idUsuario));
    }

    private Jwt jwtSinIdUsuario() {
        return jwt(Map.of("sub", "dev-user"));
    }

    private Jwt jwt(Map<String, Object> claims) {
        Instant now = Instant.now();
        return new Jwt("token", now, now.plusSeconds(3600), Map.of("alg", "none"), claims);
    }
}

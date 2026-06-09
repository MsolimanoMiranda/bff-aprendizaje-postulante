package pe.gob.oece.bff.controller.AprendizajePostulacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;
import pe.gob.oece.bff.application.AprendizajePostulacion.DetallePostulacion.DetallePostulacionQueryService;

@ExtendWith(MockitoExtension.class)
class DetallePostulacionControllerTest {

    @Mock
    DetallePostulacionQueryService detallePostulacionQueryService;

    private final JsonNode stub = new ObjectMapper().createObjectNode().put("numeroTramite", "20260001-001");

    @Test
    void obtenerDetalle_devuelve_wrapper_success_y_propaga_token() {
        DetallePostulacionController controller = new DetallePostulacionController(detallePostulacionQueryService);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/postulaciones/7/detalle");
        when(detallePostulacionQueryService.obtenerDetalle(7L, "jwt-token", "127.0.0.1")).thenReturn(stub);

        var response = controller.obtenerDetalle(7L, jwt("jwt-token"), request);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isSameAs(stub);
        assertThat(response.getInstance()).isEqualTo("/api/v1/postulaciones/7/detalle");
        verify(detallePostulacionQueryService).obtenerDetalle(7L, "jwt-token", "127.0.0.1");
    }

    @Test
    void obtenerDetalle_sin_token_responde_unauthorized() {
        DetallePostulacionController controller = new DetallePostulacionController(detallePostulacionQueryService);

        assertThatThrownBy(() -> controller.obtenerDetalle(7L, null, new MockHttpServletRequest()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Token requerido");
    }

    private Jwt jwt(String token) {
        Instant now = Instant.now();
        return new Jwt(token, now, now.plusSeconds(3600), Map.of("alg", "none"), Map.of("sub", "dev-user"));
    }
}

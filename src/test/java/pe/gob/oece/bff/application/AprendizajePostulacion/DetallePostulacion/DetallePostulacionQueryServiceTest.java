package pe.gob.oece.bff.application.AprendizajePostulacion.DetallePostulacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.DetallePostulacionClient;

@ExtendWith(MockitoExtension.class)
class DetallePostulacionQueryServiceTest {

    @Mock
    DetallePostulacionClient detallePostulacionClient;

    @InjectMocks
    DetallePostulacionQueryService service;

    private final JsonNode stub = new ObjectMapper().createObjectNode().put("idPostulacion", 7);

    @Test
    void obtenerDetalle_delega_al_client_con_id_y_token() {
        when(detallePostulacionClient.obtenerDetalle(7L, "jwt-token")).thenReturn(stub);

        JsonNode response = service.obtenerDetalle(7L, "jwt-token", "127.0.0.1");

        assertThat(response).isSameAs(stub);
        verify(detallePostulacionClient).obtenerDetalle(7L, "jwt-token");
    }
}

package pe.gob.oece.bff.controller.AprendizajePostulacion.ReprogramacionExamenes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pe.gob.oece.bff.application.AprendizajePostulacion.ReprogramacionExamenes.ReprogramacionExamenesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.config.GlobalExceptionHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReprogramacionExamenesController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "app.clients.aprendizaje-postulacion.base-url=http://fake-url"
})
@ActiveProfiles({"test", "dev"})
@org.springframework.context.annotation.Import(GlobalExceptionHandler.class)
class ReprogramacionExamenesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReprogramacionExamenesService service;

    @Test
    void buscarProgramacionesDisponibles_responde_200() throws Exception {
        ObjectNode data = objectMapper.createObjectNode().put("totalElementos", 1);
        when(service.buscarProgramacionesDisponibles(any(), any(), any())).thenReturn(data);

        mockMvc.perform(get(ApiPaths.REPROGRAMACION_EXAMENES + "/programaciones-disponibles")
                        .header("Authorization", "Bearer token")
                        .param("idLocal", "1")
                        .param("fechaDesde", "2026-06-01")
                        .param("fechaHasta", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.totalElementos").value(1));
    }

    @Test
    void registrarReprogramacion_responde_200() throws Exception {
        ObjectNode data = objectMapper.createObjectNode().put("rpta", 1);
        when(service.registrarReprogramacion(any(), any(), any())).thenReturn(data);

        mockMvc.perform(post(ApiPaths.REPROGRAMACION_EXAMENES + "/registrar")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idSolicitudReprog\":10,\"idProgExamenNueva\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.rpta").value(1));
    }

    @Test
    void registrarReprogramacion_sin_ids_responde_400() throws Exception {
        mockMvc.perform(post(ApiPaths.REPROGRAMACION_EXAMENES + "/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

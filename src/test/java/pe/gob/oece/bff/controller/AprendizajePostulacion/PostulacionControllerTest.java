package pe.gob.oece.bff.controller.AprendizajePostulacion;

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
import pe.gob.oece.bff.application.AprendizajePostulacion.PostulacionesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.config.GlobalExceptionHandler;
import pe.gob.oece.bff.domain.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostulacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "app.clients.aprendizaje-postulacion.base-url=http://fake-url"
})
@ActiveProfiles({"test", "dev"})
@org.springframework.context.annotation.Import(GlobalExceptionHandler.class)
class PostulacionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostulacionesService postulacionesService;

    @Test
    void iniciarPostulacion_responde_201_con_ApiWrapper_created() throws Exception {
        ObjectNode data = objectMapper.createObjectNode().put("id", 1);
        when(postulacionesService.iniciarPostulacion(eq(42L), any(), any())).thenReturn(data);

        String body = "{\"aceptaTerminos\":true,\"fechaHoraAceptacion\":\"2026-05-08T00:00:00\"}";

        mockMvc.perform(post(ApiPaths.POSTULACIONES)
                        .header("X-Postulante-Id", "42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.type").value("created"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void listarMisPostulaciones_responde_200_con_ApiWrapper_success() throws Exception {
        ObjectNode data = objectMapper.createObjectNode().put("total", 0);
        when(postulacionesService.listarMisPostulaciones(eq(42L), any())).thenReturn(data);

        mockMvc.perform(get(ApiPaths.POSTULACIONES).header("X-Postulante-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.total").value(0));
    }

    @Test
    void obtenerDetalle_404_propaga_NotFound() throws Exception {
        when(postulacionesService.obtenerDetalle(eq(999L), eq(42L), any()))
                .thenThrow(new NotFoundException("Postulación 999 no existe"));

        mockMvc.perform(get(ApiPaths.POSTULACIONES + "/{id}", 999)
                        .header("X-Postulante-Id", "42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Recurso no encontrado"));
    }

    @Test
    void eliminarExperiencia_responde_200() throws Exception {
        when(postulacionesService.eliminarExperiencia(eq(7L), eq(99L), eq(42L), any()))
                .thenReturn(objectMapper.createObjectNode());

        mockMvc.perform(delete(ApiPaths.POSTULACIONES + "/{id}/experiencias/{idExp}", 7, 99)
                        .header("X-Postulante-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detail").value("Experiencia laboral eliminada"));
    }

    @Test
    void iniciarPostulacion_sin_header_responde_400() throws Exception {
        mockMvc.perform(post(ApiPaths.POSTULACIONES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"aceptaTerminos\":true,\"fechaHoraAceptacion\":\"2026-05-08\"}"))
                .andExpect(status().isBadRequest());
    }
}

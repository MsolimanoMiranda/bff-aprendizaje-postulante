package pe.gob.oece.bff.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExceptionThrowerTestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@TestPropertySource(properties = {
        "app.clients.aprendizaje-postulacion.base-url=http://fake-url"
})
@ActiveProfiles({"test", "dev"})
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void notFoundException_devuelve_404_problemDetail() throws Exception {
        mockMvc.perform(get("/__test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.detail").value("recurso missing"))
                .andExpect(jsonPath("$.type").value("https://oece.gob.pe/errors/NOT-FOUND"));
    }

    @Test
    void domainException_devuelve_400_y_extrae_detail_del_body_downstream() throws Exception {
        mockMvc.perform(get("/__test/domain-detalle"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Ocurrió un error inesperado."));
    }

    @Test
    void domainException_extrae_message_si_no_hay_detail() throws Exception {
        mockMvc.perform(get("/__test/domain-message"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("input invalido"));
    }

    @Test
    void domainException_devuelve_body_crudo_si_no_es_json() throws Exception {
        mockMvc.perform(get("/__test/domain-raw"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("error sin formato"));
    }

    @Test
    void webClientResponseException_500_se_traduce_a_502() throws Exception {
        mockMvc.perform(get("/__test/upstream-500"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.type").value("https://oece.gob.pe/errors/UPSTREAM-ERROR"));
    }

    @Test
    void exception_generica_devuelve_500_internal_error() throws Exception {
        mockMvc.perform(get("/__test/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.type").value("https://oece.gob.pe/errors/INTERNAL-ERROR"));
    }
}

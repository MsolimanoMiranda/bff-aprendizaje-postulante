package pe.gob.oece.bff.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.config.ApiPaths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource(properties = {
        "app.clients.aprendizaje-postulacion.base-url=http://fake-url"
})
@WebMvcTest(ConnectivityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({"test", "dev"})
class ConnectivityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean(name = "aprendizajePostulacionWebClient")
    WebClient aprendizajePostulacionWebClient;

    @Test
    void connectivityEndpointEsAccesibleSinAutenticacion() throws Exception {
        mockMvc.perform(get(ApiPaths.CONNECTIVITY))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 200 && status != 503) {
                        throw new AssertionError("Se esperaba 200 o 503, pero fue: " + status);
                    }
                })
                .andExpect(jsonPath("$.aprendizaje-postulacion").exists())
                .andExpect(jsonPath("$.aprendizaje-postulacion.status").exists())
                .andExpect(jsonPath("$.aprendizaje-postulacion.url").exists());
    }
}

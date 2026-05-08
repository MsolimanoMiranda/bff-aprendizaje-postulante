package pe.gob.oece.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles({"test", "dev"})
@TestPropertySource(properties = {
        "app.clients.aprendizaje-postulacion.base-url=http://fake-url"
})
class BffAprendizajePostulanteApplicationTest {

    @Test
    void contextLoads() {
    }
}

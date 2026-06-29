package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PostulacionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ExperienciaLaboralRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.GenerarOrdenPagoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.SeleccionarProgramacionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostulacionesServiceTest {

    private static final String IP = "203.0.113.5";
    private static final String TOKEN = "jwt-token";

    @Mock
    PostulacionClient postulacionClient;

    @InjectMocks
    PostulacionesService service;

    private final JsonNode stub = new ObjectMapper().createObjectNode().put("ok", true);

    @Test
    void iniciarPostulacion_delega() {
        IniciarPostulacionRequest req = new IniciarPostulacionRequest(true, "2026-05-08");
        when(postulacionClient.iniciarPostulacion(42L, req)).thenReturn(stub);

        assertThat(service.iniciarPostulacion(42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).iniciarPostulacion(42L, req);
    }

    @Test
    void listarMisPostulaciones_delega() {
        when(postulacionClient.listarMisPostulaciones(42L, TOKEN)).thenReturn(stub);

        assertThat(service.listarMisPostulaciones(42L, TOKEN, IP)).isSameAs(stub);
        verify(postulacionClient).listarMisPostulaciones(42L, TOKEN);
    }

    @Test
    void obtenerDetalle_delega() {
        when(postulacionClient.obtenerDetalle(7L, 42L)).thenReturn(stub);

        assertThat(service.obtenerDetalle(7L, 42L, IP)).isSameAs(stub);
        verify(postulacionClient).obtenerDetalle(7L, 42L);
    }

    @Test
    void agregarExperiencia_delega() {
        ExperienciaLaboralRequest req = new ExperienciaLaboralRequest(
                1L, "X", "A", "C", "2024-01-01", null, "S", null, null, null
        );
        when(postulacionClient.agregarExperiencia(7L, 42L, req)).thenReturn(stub);

        assertThat(service.agregarExperiencia(7L, 42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).agregarExperiencia(7L, 42L, req);
    }

    @Test
    void editarExperiencia_delega() {
        ExperienciaLaboralRequest req = new ExperienciaLaboralRequest(
                1L, "X", "A", "C", "2024-01-01", null, "N", null, null, null
        );
        when(postulacionClient.editarExperiencia(7L, 99L, 42L, req)).thenReturn(stub);

        assertThat(service.editarExperiencia(7L, 99L, 42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).editarExperiencia(7L, 99L, 42L, req);
    }

    @Test
    void eliminarExperiencia_delega() {
        when(postulacionClient.eliminarExperiencia(7L, 99L, 42L)).thenReturn(stub);

        assertThat(service.eliminarExperiencia(7L, 99L, 42L, IP)).isSameAs(stub);
        verify(postulacionClient).eliminarExperiencia(7L, 99L, 42L);
    }

    @Test
    void seleccionarProgramacion_delega() {
        SeleccionarProgramacionRequest req = new SeleccionarProgramacionRequest(123L);
        when(postulacionClient.seleccionarProgramacion(7L, 42L, req)).thenReturn(stub);

        assertThat(service.seleccionarProgramacion(7L, 42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).seleccionarProgramacion(7L, 42L, req);
    }

    @Test
    void generarOrdenPago_delega() {
        GenerarOrdenPagoRequest req = new GenerarOrdenPagoRequest("NIUBIZ");
        when(postulacionClient.generarOrdenPago(7L, 42L, req)).thenReturn(stub);

        assertThat(service.generarOrdenPago(7L, 42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).generarOrdenPago(7L, 42L, req);
    }

    @Test
    void confirmarPostulacion_delega() {
        ConfirmarPostulacionRequest req = new ConfirmarPostulacionRequest(555L);
        when(postulacionClient.confirmarPostulacion(7L, 42L, req)).thenReturn(stub);

        assertThat(service.confirmarPostulacion(7L, 42L, req, IP)).isSameAs(stub);
        verify(postulacionClient).confirmarPostulacion(7L, 42L, req);
    }
}

package pe.gob.oece.bff.application.AprendizajePostulacion.CursoAsignado;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.CertificacionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.CursoAsignadoClient;

@RequiredArgsConstructor
@Service
public class CursoAsignadoQueryService {
    private final CursoAsignadoClient cursoAsignadoClient;
    public JsonNode obtenerMisCursos(Long idPostulante){
        return cursoAsignadoClient.misCursos(idPostulante);
    }
}

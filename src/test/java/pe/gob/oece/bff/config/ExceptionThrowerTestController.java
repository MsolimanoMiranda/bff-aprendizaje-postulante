package pe.gob.oece.bff.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.gob.oece.bff.domain.DomainException;
import pe.gob.oece.bff.domain.NotFoundException;

@RestController
public class ExceptionThrowerTestController {

    @GetMapping("/__test/not-found")
    public String nf() {
        throw new NotFoundException("recurso missing");
    }

    @GetMapping("/__test/domain-detalle")
    public String detalle() {
        throw new DomainException("X-001",
                "{\"detalle\":\"Ocurrió un error inesperado.\"}");
    }

    @GetMapping("/__test/domain-message")
    public String message() {
        throw new DomainException("X-002", "{\"message\":\"input invalido\"}");
    }

    @GetMapping("/__test/domain-raw")
    public String raw() {
        throw new DomainException("X-003", "error sin formato");
    }

    @GetMapping("/__test/upstream-500")
    public String upstream() {
        throw WebClientResponseException.create(500, "boom", null,
                "{\"detail\":\"db down\"}".getBytes(), null);
    }

    @GetMapping("/__test/boom")
    public String boom() {
        throw new IllegalStateException("crash interno");
    }
}

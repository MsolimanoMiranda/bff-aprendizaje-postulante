package pe.gob.oece.bff.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.clients.control-archivos")
public class ControlArchivosPropiedades {
    private String baseUrl;
    private String publicKey;
    private String apiKey;
    private String jwtToken;

    private Integer connectTimeoutMs;
    private Integer readTimeoutMs;
    private Integer responseTimeoutMs;
}

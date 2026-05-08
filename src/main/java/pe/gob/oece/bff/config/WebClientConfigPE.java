package pe.gob.oece.bff.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfigPE {

    @Value("${app.clients.aprendizaje-postulacion.base-url}")
    private String postulacionBaseUrl;

    @Value("${app.clients.aprendizaje-postulacion.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Value("${app.clients.aprendizaje-postulacion.read-timeout-ms:5000}")
    private int readTimeoutMs;

    @Bean("aprendizajePostulacionWebClient")
    public WebClient aprendizajePostulacionWebClient() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
            .responseTimeout(Duration.ofMillis(readTimeoutMs))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
            );

        return WebClient.builder()
            .baseUrl(java.util.Objects.requireNonNull(postulacionBaseUrl))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(java.util.Objects.requireNonNull(httpClient)))
            .build();
    }

}

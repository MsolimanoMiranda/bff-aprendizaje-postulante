package pe.gob.oece.bff.infrastructure.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class DownstreamWebClient {

    private final WebClient webClient;
    private final Duration timeout;
    private final DownstreamClientErrorHandler errorHandler;

    public DownstreamWebClient(
            WebClient webClient,
            Duration timeout,
            DownstreamClientErrorHandler errorHandler
    ) {
        this.webClient = webClient;
        this.timeout = timeout;
        this.errorHandler = errorHandler;
    }

    public <T> T get(String uri, Map<String, String> headers, Class<T> responseType, Object... uriVars) {
        return decode(
                webClient.get()
                        .uri(uri, uriVars)
                        .headers(applyHeaders(headers))
                        .retrieve(),
                responseType
        );
    }

    public <T> T get(String uri, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Object... uriVars) {
        return decode(
                webClient.get()
                        .uri(uri, uriVars)
                        .headers(applyHeaders(headers))
                        .retrieve(),
                responseType
        );
    }

    public <T> T get(
            Function<UriBuilder, URI> uriFunction,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        return decode(
                webClient.get()
                        .uri(uriFunction)
                        .headers(applyHeaders(headers))
                        .retrieve(),
                responseType
        );
    }

    public <T> T get(
            Function<UriBuilder, URI> uriFunction,
            Map<String, String> headers,
            ParameterizedTypeReference<T> responseType
    ) {
        return decode(
                webClient.get()
                        .uri(uriFunction)
                        .headers(applyHeaders(headers))
                        .retrieve(),
                responseType
        );
    }

    public <T> T post(String uri, Object body, Map<String, String> headers, Class<T> responseType, Object... uriVars) {
        return decode(
                retrieveWithBody(
                        webClient.post().uri(uri, uriVars).headers(applyHeaders(headers)),
                        body
                ),
                responseType
        );
    }

    public <T> T post(
            Function<UriBuilder, URI> uriFunction,
            Object body,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        return decode(
                retrieveWithBody(
                        webClient.post().uri(uriFunction).headers(applyHeaders(headers)),
                        body
                ),
                responseType
        );
    }

    public <T> T put(String uri, Object body, Map<String, String> headers, Class<T> responseType, Object... uriVars) {
        return decode(
                retrieveWithBody(
                        webClient.put().uri(uri, uriVars).headers(applyHeaders(headers)),
                        body
                ),
                responseType
        );
    }

    public <T> T delete(String uri, Map<String, String> headers, Class<T> responseType, Object... uriVars) {
        return decode(
                webClient.delete()
                        .uri(uri, uriVars)
                        .headers(applyHeaders(headers))
                        .retrieve(),
                responseType
        );
    }

    private <T> T decode(WebClient.ResponseSpec responseSpec, Class<T> responseType) {
        return responseSpec
                .onStatus(HttpStatusCode::isError, errorHandler::toError)
                .bodyToMono(responseType)
                .timeout(timeout)
                .block();
    }

    private <T> T decode(WebClient.ResponseSpec responseSpec, ParameterizedTypeReference<T> responseType) {
        return responseSpec
                .onStatus(HttpStatusCode::isError, errorHandler::toError)
                .bodyToMono(responseType)
                .timeout(timeout)
                .block();
    }

    private static WebClient.ResponseSpec retrieveWithBody(WebClient.RequestBodySpec spec, Object body) {
        return (body == null ? spec : spec.bodyValue(body)).retrieve();
    }

    private Consumer<HttpHeaders> applyHeaders(Map<String, String> headers) {
        return h -> {
            if (headers != null) {
                headers.forEach(h::set);
            }
        };
    }
}

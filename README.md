# BFF Aprendizaje y Certificación

Backend for Frontend (BFF) del dominio **Aprendizaje y Certificación** de OECE. Actúa como capa de orquestación entre los clientes (web/móvil) y los microservicios internos, delegando todas las operaciones vía **WebClient** sin persistencia propia. Construido con Spring Boot 3 y Java 21.

---

## Stack tecnológico

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.2.3 |
| Build | Gradle 8 |
| HTTP cliente | Spring WebFlux WebClient |
| Seguridad | Spring Security + OAuth2 Resource Server (JWT) |
| Resiliencia | Resilience4j (Circuit Breaker, Retry, Time Limiter) |
| Documentación | Springdoc OpenAPI 2.3.0 |
| Métricas | Micrometer + Prometheus |
| Trazabilidad | Micrometer Tracing Bridge OTEL |
| Logging | Logstash Logback Encoder (JSON estructurado) |
| Testing | JUnit 5 · Mockito · Spring Security Test · Reactor Test |
| Cobertura | JaCoCo |
| Observabilidad local | Prometheus · Loki · Promtail · Grafana |

---

## Arquitectura

Este BFF **no tiene base de datos**. Toda la lógica de acceso a datos se delega hacia los microservicios internos mediante WebClient con manejo de resiliencia.

```
Cliente (web/móvil)
        │
        ▼
┌─────────────────────────────────┐
│     BFF Aprendizaje Cert.       │
│  ┌──────────┐  ┌─────────────┐  │
│  │Controller│→ │ QueryService│  │
│  └──────────┘  └──────┬──────┘  │
│                       │         │
│               ┌───────▼───────┐ │
│               │  WebClient    │ │
│               │  + Circuit    │ │
│               │    Breaker    │ │
│               └───────────────┘ │
└─────────────────────────────────┘
        │
        ▼
ms-aprendizaje-planificacion-examenes
```

### Estructura de paquetes

```
pe.gob.oece.bff
├── BffAprendizajePostulanteApplication.java
├── application/          # DTOs de respuesta y servicios de orquestación
├── config/               # WebClient, Security, CorrelationId, ExceptionHandler
├── controller/           # Endpoints REST
├── domain/               # Excepciones de dominio
├── infrastructure/
│   └── client/           # Clientes WebClient hacia microservicios
└── shared/               # ApiProblemDetail, CorrelationIdContext
```

---

## Variables de entorno

| Variable | Descripción | Default |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`dev`, `prod`) | `dev` |
| `PLANIFICACION_EXAMENES_SERVICE_URL` | URL base del ms de planificación de exámenes | `http://localhost:8080` |
| `CONNECT_TIMEOUT_MS` | Timeout de conexión WebClient (ms) | `3000` |
| `READ_TIMEOUT_MS` | Timeout de lectura WebClient (ms) | `5000` |
| `RESPONSE_TIMEOUT_MS` | Timeout de respuesta total WebClient (ms) | `8000` |

---

## Endpoints

### Planificación de Exámenes

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/planificacion-examenes/matriz` | Bearer JWT | Lista la matriz de competencias paginada |

**Parámetros de query:**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `page` | int | `1` | Número de página (mínimo 1) |
| `size` | int | `20` | Elementos por página (máximo 100) |
| `sort` | string | `id,desc` | Campo y dirección de ordenamiento |

**Respuesta 200:**
```json
{
  "data": [
    {
      "id": 103,
      "nombre": "Matriz de Competencias 2026",
      "descripcion": "...",
      "estadoId": 1,
      "estado": "Activo",
      "estadoMatrizId": 2,
      "estadoMatriz": "En revisión"
    }
  ],
  "meta": {
    "page": 1,
    "size": 20,
    "totalElements": 45,
    "totalPages": 3
  },
  "links": {
    "self": "...",
    "first": "...",
    "prev": null,
    "next": "...",
    "last": "..."
  }
}
```

### Conectividad

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/connectivity` | Ninguna | Verifica el estado de conexión con cada microservicio |

**Respuesta 200 (todos UP):**
```json
{
  "aprendizaje-planificacion-examenes": {
    "status": "UP",
    "remoteStatus": "UP",
    "url": "http://ms-planificacion/actuator/health",
    "responseTimeMs": 38
  }
}
```

Retorna `503` si algún servicio no responde dentro de 5 segundos.

> Para agregar más servicios al check, añadir entradas en `ConnectivityController.java` siguiendo el comentario `//add new services here...`

### Operaciones

| Método | Ruta | Auth |
|---|---|---|
| `GET` | `/actuator/health` | Pública |
| `GET` | `/actuator/metrics` | `OECE_TECHOPS` |
| `GET` | `/actuator/prometheus` | `OECE_TECHOPS` |

---

## Seguridad

La seguridad es **stateless** basada en JWT (OAuth2 Resource Server).

| Ruta | Acceso |
|---|---|
| `/api/v1/connectivity` | Público (sin token) |
| `/swagger-ui/**`, `/api-docs/**` | Público |
| `/actuator/**`, `/ops/**` | Rol `OECE_TECHOPS` |
| `/api/**` | Autenticado (cualquier rol válido) |

### Perfil `dev` y `test`

Con los perfiles `dev` o `test` activos, `DevSecurityConfig` registra un `JwtDecoder` de desarrollo que **acepta cualquier string como Bearer token** y otorga los roles:

- `OECE_TECHOPS`
- `OECE_ANALYST`

```bash
curl -H "Authorization: Bearer dev-token" http://localhost/api/v1/planificacion-examenes/matriz
```

---

## Resiliencia

El circuit breaker `planificacionExamenesBackend` protege las llamadas al microservicio de planificación:

| Parámetro | Valor |
|---|---|
| Ventana deslizante | 20 llamadas |
| Umbral de fallo | 50% |
| Tiempo abierto | 15 s |
| Llamadas en semi-abierto | 5 |
| Timeout por llamada | 10 s |
| Reintentos | 3 (intervalo 200 ms) |

---

## Ejecución local

### Solo la aplicación

```bash
./gradlew bootRun
```

La app arranca en el puerto **80** con perfil `dev` por defecto.

### Con stack de observabilidad (Docker Compose)

```bash
docker compose up -d
```

En Docker Compose la aplicación escribe el archivo de log en `/tmp` dentro del contenedor para evitar problemas de permisos con volúmenes bind-mounted. Promtail sigue recolectando los logs del contenedor por stdout y la carpeta local `./logs` queda para ejecuciones locales con `bootRun`.

| Servicio | URL |
|---|---|
| Aplicación | http://localhost |
| Swagger UI | http://localhost/swagger-ui/index.html |
| Connectivity | http://localhost/api/v1/connectivity |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

---

## Tests

```bash
# Ejecutar tests
./gradlew test

# Limpiar y re-ejecutar desde cero
./gradlew cleanTest test

# Generar reporte de cobertura
./gradlew test jacocoTestReport
```

### Suite incluida

| Clase | Tipo | Tests |
|---|---|---|
| `BffAprendizajePostulanteApplicationTest` | Contexto Spring (`@SpringBootTest`) | 1 |
| `PlanificacionExamenesQueryServiceTest` | Unitario (Mockito) | 2 |
| `PlanificacionExamenesControllerTest` | Web slice (`@WebMvcTest`) | 3 |
| `ConnectivityControllerTest` | Web slice (`@WebMvcTest`) | 1 |
| `PlanificacionExamenClientTest` | Unitario (WebClient mock) | 2 |

### Reportes

- Tests: `build/reports/tests/test/index.html`
- Cobertura JaCoCo: `build/reports/jacoco/test/html/index.html`

---

## Observabilidad

### Correlation ID

Cada request recibe (o genera) automáticamente un `X-Correlation-Id`. El valor se propaga en los headers de salida y en todos los logs como campo MDC `correlationId`.

### Logs estructurados

Los logs se emiten en formato JSON (Logstash Logback Encoder) con los campos `correlationId`, `traceId` y `spanId`, listos para ingestión en Loki/Grafana.

### Métricas

El endpoint `/actuator/prometheus` expone métricas Micrometer en formato Prometheus, con histogramas de latencia y SLOs predefinidos (50ms, 100ms, 200ms, 300ms, 500ms, 1s, 2s).

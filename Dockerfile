# Build stage
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradle.properties ./
COPY src/ src/

RUN gradle bootJar --no-daemon --no-watch-fs

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S oece && adduser -S oece -G oece

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p /app/logs && chown -R oece:oece /app

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health/liveness || exit 1

USER oece:oece

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

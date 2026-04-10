# Multi-stage Dockerfile for Document Service
# Stage 1: Build with Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml và download dependencies (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build JAR file
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime with minimal image
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="Document Service - PDF Ticket Generation" \
      version="1.0"

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Environment variables (có thể override khi chạy container)
ENV SPRING_PROFILES_ACTIVE=production \
    SERVER_PORT=8081 \
    SERVICE_DOCUMENT_PORT=8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/health || exit 1

# Run application
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]

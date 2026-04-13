FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
RUN chmod +x mvnw

COPY src src
RUN ./mvnw -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

# Optional build-time defaults (can be overridden at runtime with -e/--env-file)
ARG DB_INV_HOST=127.0.0.1
ARG DB_INVENTORY_PORT=3306
ARG DB_INVENTORY_NAME=inventory_db
ARG DB_INVENTORY_USER=root
ARG EUREKA_SERVER=http://localhost:8761/eureka/

ENV DB_INV_HOST=${DB_INV_HOST} \
    DB_INVENTORY_PORT=${DB_INVENTORY_PORT} \
    DB_INVENTORY_NAME=${DB_INVENTORY_NAME} \
    DB_INVENTORY_USER=${DB_INVENTORY_USER} \
    EUREKA_SERVER=${EUREKA_SERVER}

EXPOSE 8082
ENV SERVER_INVENTORY_PORT=8082

ENTRYPOINT ["java", "-jar", "app.jar"]

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

COPY --from=builder /app/target/tour-services-0.0.1-SNAPSHOT.jar app.jar

# Optional build-time defaults (can be overridden at runtime with -e/--env-file)
ARG DB_HOST=127.0.0.1
ARG DB_PORT=3306
ARG DB_NAME=tour_db
ARG DB_USER=root
ARG DB_PASSWORD=

ENV DB_HOST=${DB_HOST} \
    DB_PORT=${DB_PORT} \
    DB_NAME=${DB_NAME} \
    DB_USER=${DB_USER} \
    DB_PASSWORD=${DB_PASSWORD}

EXPOSE 5002
ENV SERVER_PORT=5002

ENTRYPOINT ["java", "-jar", "app.jar"]

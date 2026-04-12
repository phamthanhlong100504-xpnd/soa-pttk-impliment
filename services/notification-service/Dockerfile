FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
RUN chmod +x mvnw

COPY src src
RUN ./mvnw -Dmaven.test.skip=true clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/notification-service-0.0.1-SNAPSHOT.jar app.jar

# Optional build-time defaults (can be overridden at runtime with -e/--env-file)
ARG MAIL_HOST=smtp.gmail.com
ARG MAIL_PORT=587
ARG MAIL_USERNAME=
ARG MAIL_PASSWORD=
ARG MAIL_FROM=
ARG EUREKA_SERVER=http://eureka-server:8761/eureka/

ENV MAIL_HOST=${MAIL_HOST} \
	MAIL_PORT=${MAIL_PORT} \
	MAIL_USERNAME=${MAIL_USERNAME} \
	MAIL_PASSWORD=${MAIL_PASSWORD} \
	MAIL_FROM=${MAIL_FROM} \
	EUREKA_SERVER=${EUREKA_SERVER}

EXPOSE 8081
ENV SERVER_NOTIFICATION_PORT=8081

ENTRYPOINT ["java", "-jar", "app.jar"]

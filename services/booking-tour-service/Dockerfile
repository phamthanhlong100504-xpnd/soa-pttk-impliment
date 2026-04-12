# Bước 1: Build bằng Maven bên trong Docker
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy file pom.xml và tải dependency trước để cache
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy toàn bộ code vào và build file JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Bước 2: Chạy file JAR bằng JRE nhẹ
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy file JAR từ bước build sang
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

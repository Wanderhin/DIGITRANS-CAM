# Stage 1: Build
FROM maven:3.9.6-amazoncorretto-21 AS builder
WORKDIR /app

# Copier le pom.xml pour télécharger les dépendances (mise en cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copier le code source et compiler
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

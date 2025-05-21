# Etapa 1: Build do app com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Etapa 2: Imagem final com JDK apenas
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

COPY .env .env

ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8081


ENTRYPOINT exec sh -c "java $JAVA_OPTS -jar app.jar"

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar
COPY /etc/secrets/.env .env

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

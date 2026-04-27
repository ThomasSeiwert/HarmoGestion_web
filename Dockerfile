FROM eclipse-temurin:25
LABEL authors="cvert"

WORKDIR /app

EXPOSE 9005

COPY harmoGestionWeb-0.0.1-SNAPSHOT.jar /app/harmoGestionWeb.jar

ENTRYPOINT ["java", "-jar", "/app/harmoGestionWeb.jar"]
# Imagen base con Java 21
FROM eclipse-temurin:21-jre-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR al contenedor
COPY target/mi-bot-java-1.0.jar app.jar

# Comando para arrancar el bot
ENTRYPOINT ["java", "-jar", "app.jar"]
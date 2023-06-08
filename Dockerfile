
# Establece la imagen base de Docker
#FROM maven:3.8.4-jdk-11-slim AS build
FROM maven:3.6.3-jdk-11-slim AS build

# Establece el directorio de trabajo en el contenedor
WORKDIR /chatserver

# Copia el archivo pom.xml para descargar las dependencias
COPY pom.xml .

# Descarga las dependencias del proyecto
RUN mvn dependency:go-offline -B

# Copia los archivos fuente del proyecto
COPY src ./src

# Compila y empaqueta la aplicación
RUN mvn package -DskipTests

# Crea una nueva imagen basada en OpenJDK
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /chatserver

# Copia el archivo JAR generado en la etapa anterior
COPY --from=build /chatserver/target/chatserver.jar .

# Expone el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
CMD ["java", "-jar", "chatserver.jar"]


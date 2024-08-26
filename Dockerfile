# Use uma imagem intermediária para compilar o projeto
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Use uma imagem mais leve para a produção
FROM openjdk:21-jdk-slim

# Configuração de fuso horário (opcional, pode ser movido para a fase de build anterior para manter a imagem final leve)
RUN echo "Zone America/Brasilia -3:00 - GMT-3" > /tmp/Brasilia.zic && \
    zic /tmp/Brasilia.zic && \
    ln -sf /usr/share/zoneinfo/America/Brasilia /etc/localtime

WORKDIR /app
COPY --from=build /app/target/message-api.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

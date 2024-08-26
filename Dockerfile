FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim

RUN echo "Zone America/Brasilia -3:00 - GMT-3" > /tmp/Brasilia.zic && \
    zic /tmp/Brasilia.zic && \
    ln -sf /usr/share/zoneinfo/America/Brasilia /etc/localtime

EXPOSE 8081

COPY --from=build target/message-api.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]

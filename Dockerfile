FROM maven:3.9-amazoncorretto-21 AS build
MAINTAINER ouharrioutman@gmail.com
VOLUME /tmp
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:23-slim
WORKDIR /app
COPY --from=build app/target/fileUploader-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
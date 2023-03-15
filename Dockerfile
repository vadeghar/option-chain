FROM maven:3.8.3-jdk11 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/option-chain-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
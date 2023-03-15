FROM maven:3.8.7-eclipse-temurin-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=server/target/option-chain-*.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

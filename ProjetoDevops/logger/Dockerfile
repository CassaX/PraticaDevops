FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /logger-service

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/ROOT*

COPY --from=build /logger-service/target/logger-service.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

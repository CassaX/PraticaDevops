#  Build com Maven + JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia arquivos do projeto para dentro do container
COPY pom.xml .
COPY src ./src

# Compila e empacota o WAR
RUN mvn clean package -DskipTests

# Stage 2: Imagem final Tomcat
FROM tomcat:10.1-jdk17

# Remove a aplicação padrão do Tomcat (ROOT)
RUN rm -rf /usr/local/tomcat/webapps/ROOT*

# Copia o WAR gerado para a pasta ROOT.war para servir na raiz
COPY --from=build /app/target/app.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta 8080
EXPOSE 8080

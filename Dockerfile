FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Usa uma imagem Debian Slim (mínima)
FROM openjdk:17-jdk-slim

WORKDIR /app

# ✅ Adiciona curl e remove cache para manter a imagem leve
RUN apt update && apt install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

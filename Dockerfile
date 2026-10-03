# --- ETAPA 1: O Build (Compila o projeto na nuvem do Render) ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o código fonte
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dá permissão de execução pro maven wrapper e roda o empacotamento ignorando testes
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# --- ETAPA 2: A Execução (Roda o jar gerado) ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia apenas o jar gerado na etapa anterior para esta imagem limpa e leve
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
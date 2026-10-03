# Usa uma imagem oficial do Java 17 (ou 21, dependendo da versão que você usou)
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo jar gerado na sua pasta target para dentro do container
COPY target/*.jar app.jar

# Expõe a porta que o Spring Boot vai rodar (o Render injeta a porta automaticamente, mas por padrão usamos a 8080)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
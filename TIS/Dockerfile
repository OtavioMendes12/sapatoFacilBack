# Usar a imagem base do OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo .jar da aplicação para o contêiner
COPY target/TIS-0.0.1-SNAPSHOT.jar /app/app.jar

# Expor a porta na qual a aplicação será executada (opcional, baseado na sua aplicação)
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar"]
FROM openjdk:11.0-jre as runtime
COPY target/minesweeper-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=$ENV_PROFILE /app.jar"]

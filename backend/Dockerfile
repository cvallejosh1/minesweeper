FROM openjdk:11.0-jre as runtime
ARG REGION_ARG=us-east-2
ARG ACCESS_ARG
ARG SECRET_ARG
ENV AWS_REGION=$REGION_ARG
ENV AWS_ACCESS_KEY_ID=$ACCESS_ARG
ENV AWS_SECRET_ACCESS_KEY=$SECRET_ARG
COPY target/minesweeper-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=$ENV_PROFILE /app.jar"]

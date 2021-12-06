FROM adoptopenjdk/openjdk11:latest
EXPOSE 8080
ARG JAR_FILE=/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
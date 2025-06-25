FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar

ARG PROFILES
ARG ENV

COPY ${JAR_FILE} app.jar
ENTRYPOINT sh -c "exec java -Dspring.profiles.active=\$PROFILES -Dserver.env=\$ENV -jar app.jar"
# 1. 빌드 단계
FROM gradle:8.2.1-jdk17 AS build

USER root
RUN mkdir -p /home/gradle/.gradle && chown -R gradle:gradle /home/gradle/.gradle

COPY --chown=gradle:gradle . /app
WORKDIR /app

USER gradle
RUN ./gradlew clean build -x test

# 2. 실행 단계
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

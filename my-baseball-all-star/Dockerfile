# 1. 빌드 단계 - Gradle + JDK 21 이미지 사용 (openjdk 21 + gradle 별도 설치 가능)
FROM gradle:8.2.1-jdk21 AS build

USER root
RUN mkdir -p /home/gradle/.gradle && chown -R gradle:gradle /home/gradle/.gradle

COPY --chown=gradle:gradle . /app
WORKDIR /app

USER gradle
RUN gradle clean build -x test --no-daemon --no-build-cache

# 2. 실행 단계 - OpenJDK 21 slim 버전 사용
FROM openjdk:21-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

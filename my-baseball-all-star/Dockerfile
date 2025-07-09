# 1. 빌드 단계
FROM gradle:8.2.1-jdk17 AS build

# 권한 세팅
USER root
RUN mkdir -p /home/gradle && chown -R gradle:gradle /home/gradle

# 소스 복사
COPY --chown=gradle:gradle . /app
WORKDIR /app

# gradle로 빌드
USER gradle
RUN gradle clean build -x test --no-daemon --no-build-cache --stacktrace

# 2. 실행 단계
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

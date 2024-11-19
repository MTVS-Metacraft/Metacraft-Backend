FROM gradle:7.6.1-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel=true -Dorg.gradle.workers.max=4"

# Gradle 빌드
RUN gradle build -x test --no-daemon \
    --console=plain \
    --info \
    --stacktrace

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
COPY --from=build /home/gradle/src/src/main/resources /app/src/main/resources

EXPOSE 12453
ENTRYPOINT ["java", "-jar", "app.jar"]
# Build Stage
FROM gradle:7-jdk11 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

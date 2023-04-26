FROM openjdk:11-slim AS builder
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew && ./gradlew bootJar

FROM openjdk:11
ENV TZ="Asiz/Seoul"
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar", \
             "-Dspring.profiles.active=dev"]

FROM openjdk:11-slim AS builder
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew && ./gradlew bootJar

FROM openjdk:11-jre
ENV TZ="Asiz/Seoul"
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", \
             "-Dspring.profiles.active=dev", \
             "-Dspring.config.location=classpath:/application.yml,/app/config/application-db.yml,/app/config/application-s3.yml", \
             "/app/app.jar"]

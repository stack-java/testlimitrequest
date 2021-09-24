FROM maven:3.6.2-jdk-11 as builder
WORKDIR /app
COPY pom.xml ./
COPY src/ ./src/
RUN mvn package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "/app/app.jar"]

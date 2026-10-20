FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Keep the heap small enough for free-tier hosts (512 MB)
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]

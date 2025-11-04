# backend/Dockerfile
# Stage 1 — build with maven and cache dependencies
FROM maven:3.9.6-openjdk-17-slim AS build
WORKDIR /app
# copy only what is needed to leverage docker cache for deps
COPY pom.xml .
COPY mvnw .
# if you use wrapper and .mvn
COPY .mvn .mvn
RUN mvn -B -f pom.xml dependency:go-offline

# copy sources and build
COPY src ./src
RUN mvn -B -f pom.xml package -DskipTests

# Stage 2 — runtime
FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=/app/target/*.jar
# create non-root user
RUN addgroup --system appgroup && adduser --system appuser && mkdir /app
USER appuser
WORKDIR /app
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xms128m","-Xmx512m","-jar","/app/app.jar"]

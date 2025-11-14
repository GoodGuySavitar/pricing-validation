# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
COPY .mvn ./.mvn
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Create non-root user
RUN groupadd -r spring \
 && useradd -r -g spring -s /sbin/nologin -d /nonexistent spring \
 && chown spring:spring /app/app.jar

USER spring:spring

EXPOSE 8080

ENV TZ=UTC

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-jar", "app.jar"]

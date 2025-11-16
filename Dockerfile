# Use Eclipse Temurin JDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x gradlew

# Build the application (skip tests for faster builds)
RUN ./gradlew bootJar --no-daemon

# Runtime stage - smaller image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set environment variables (can be overridden by docker-compose)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://percona:3306/securedb?useSSL=false&allowPublicKeyRetrieval=true
ENV SPRING_DATASOURCE_USERNAME=comp3335
ENV SPRING_DATASOURCE_PASSWORD=secure_password

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

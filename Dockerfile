# -------- Stage 1: Build the app --------
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Copy everything to container
COPY . .

# Build the app (skip tests for faster build)
RUN gradle bootJar -x test

# -------- Stage 2: Run the app --------
FROM eclipse-temurin:17
WORKDIR /app

# Copy the Spring Boot fat jar explicitly
COPY --from=builder /app/build/libs/app.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

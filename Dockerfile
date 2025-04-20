# Stage 1: Build the JAR (using Maven)
FROM eclipse-temurin:24-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline   # Cache dependencies
COPY src ./src
RUN ./mvnw package -DskipTests    # Build the JAR (skip tests for speed)

# Stage 2: Run the JAR (smaller image, no build tools)
FROM eclipse-temurin:24-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
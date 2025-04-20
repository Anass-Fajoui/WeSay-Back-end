# Stage 1: Build the JAR (using Maven)
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app

# Copy and prepare Maven wrapper first
COPY mvnw ./
COPY .mvn/ .mvn
RUN chmod +x mvnw  # Ensure the wrapper is executable

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B  # -B for batch mode (non-interactive)

# Copy source code and build
COPY src ./src
RUN ./mvnw package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
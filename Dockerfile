# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY . /app

# Run Maven commands to clean, package, and download dependencies
RUN mvn clean
RUN mvn install
RUN mvn package

# Stage 2: Create the final image
FROM openjdk:17-slim

# Set the working directory for the application
WORKDIR /app

# Copy the JAR file and dependencies from the builder stage
COPY --from=builder /app/agent/target/agent-1.0-SNAPSHOT.jar /app/agent.jar
COPY --from=builder /app/application/target/application-1.0-SNAPSHOT.jar /app/application.jar

# Run the application with the specified Java agent and JVM options
CMD ["java", "-javaagent:/app/agent.jar=HT_MODE=Record", "-Ddebug", "-jar", "/app/application.jar"]
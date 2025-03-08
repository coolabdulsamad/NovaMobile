# Use an official OpenJDK 22 runtime as a parent image
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Install Maven and dependencies
RUN apt-get update && apt-get install -y maven

# Copy the full project into the container
COPY . .

# Build the project
RUN mvn clean package

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the app directly from the build location
CMD ["java", "-jar", "/app/target/ChatApplication-1.0-SNAPSHOT.jar"]

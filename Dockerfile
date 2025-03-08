# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY . .

# Install Maven and build the project
RUN apt-get update && apt-get install -y maven && mvn clean package

# Copy the built JAR to the container
COPY target/NovaMobile.jar /app/NovaMobile.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the app
CMD ["java", "-jar", "/app/NovaMobile.jar"]

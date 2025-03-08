# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/NovaMobile.jar /app/NovaMobile.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the app
CMD ["java", "-jar", "NovaMobile.jar"]

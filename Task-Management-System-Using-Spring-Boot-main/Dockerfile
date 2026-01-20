# Use the official OpenJDK as a parent image
FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the host to the container
ADD ./target/taskmanagement-0.0.1-SNAPSHOT.jar /app/taskmanagement-0.0.1-SNAPSHOT.jar

# Expose the port on which the Spring Boot app listens
EXPOSE 8090

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "taskmanagement-0.0.1-SNAPSHOT.jar"]
# Use an official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target folder (replace `your-app.jar` with your actual file name)
COPY target/todo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will run on (default Spring Boot port is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

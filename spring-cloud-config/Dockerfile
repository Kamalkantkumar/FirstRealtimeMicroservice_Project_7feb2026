# Use a lightweight Java 17 image (Matches your discovery-server setup)
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from your target folder into the container
COPY target/*.jar app.jar

# Expose the standard Config Server port
EXPOSE 8888

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
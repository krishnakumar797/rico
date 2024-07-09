# Use an official Java runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory in the container to /app
WORKDIR /app

# Copy the jar file from your local machine to the working directory in the container
COPY build/libs/app.jar app.jar

# Expose the port that your application will be listening on
EXPOSE 8762

# Run the application
CMD ["java", "-jar", "app.jar"]
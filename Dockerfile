# Use lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy built jar file
COPY target/querynexus-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 9090

# Run application
ENTRYPOINT ["java","-jar","app.jar"]

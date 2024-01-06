# Use the Maven image for building the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory to /app
WORKDIR /app

# Copy the contents of the current directory to /app in the image
COPY . .

# Run the Maven build within the /app directory
RUN mvn clean package -DskipTests

# Use the smaller OpenJDK image for the final runtime image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the compiled JAR file from the build image
COPY --from=build /app/InfoSphere/target/demo.jar demo.jar

# Expose the application port
EXPOSE 8080

# Specify the entry point for the application
ENTRYPOINT ["java", "-jar", "demo.jar"]

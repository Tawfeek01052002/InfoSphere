# Use the Maven image for building the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory to /InfoSphere
WORKDIR /InfoSphere

# Copy the Maven configuration files first to leverage Docker cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy the source code
COPY src src

# Run the Maven build within the /InfoSphere directory
RUN mvn clean package -DskipTests

# Use the smaller OpenJDK image for the final runtime image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory to /InfoSphere
WORKDIR /InfoSphere

# Copy the compiled JAR file from the build image
COPY --from=build /InfoSphere/target/demo.jar demo.jar

# Expose the application port
EXPOSE 8080

# Specify the entry point for the application
ENTRYPOINT ["java", "-jar", "demo.jar"]

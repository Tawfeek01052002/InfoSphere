FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory to the location of the project's POM file
WORKDIR /InfoSphere

# Copy only the necessary files into the container
COPY pom.xml .

# Run Maven build
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

# Set the working directory for the application
WORKDIR /InfoSphere

# Copy the JAR file from the build stage to the final image
COPY --from=build /InfoSphere/target/demo.jar demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]

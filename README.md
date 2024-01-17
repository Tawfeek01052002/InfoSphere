# Infosphere - Web-Based Contact Management Application

Infosphere is a robust web-based contact management application developed using the Spring Boot framework with Thymeleaf for dynamic HTML rendering. The application is secured with Spring Security, and it utilizes an embedded Tomcat server for seamless deployment. PostgreSQL serves as the backend database, and Maven simplifies project management.

**Live Demo:** [Infosphere on Render](https://infospheres.onrender.com)

## Features

- **Contact Management**: Effortlessly store and manage your contacts.
- **Thymeleaf Templates**: Utilizes Thymeleaf for dynamic and expressive HTML rendering.
- **Embedded Tomcat Server**: Seamless deployment and testing with the embedded Tomcat server.
- **Spring Security**: Secure your application with Spring Security features.
- **PostgreSQL Database**: Reliable and scalable backend data storage.
- **Maven Project**: Streamlined dependency management and build processes.

## Prerequisites

Before you begin, ensure you have the following prerequisites:

- Java Development Kit (JDK)
- Springboot
- Thymeleaf
- Maven
- Docker
- PostgreSQL

## Getting Started

### Configuration

1. **Clone the repository:**

   ```bash
   https://github.com/Tawfeek01052002/InfoSphere.git
   ```
2. **Configure PostgreSQL:** * Create a PostgreSQL database for Infosphere.

* Update `src/main/resources/application.properties` with your database configuration.

### Build and Run

###### Maven Build

```
mvn clean package
```

**Create Dockerfile**

* Create a file named Dockerfile in the project root. Copy and paste the following Docker commands into the file. This will be used to create the Docker image, that we are going to deploy on Render.

```Dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Run: `docker build -t demo .` and replace `demo` with your own project name.

### Docker Run

```
docker run -p 8080:8080 infosphere
```

Visit [http://localhost:8080](http://localhost:8080/) to access Infosphere locally.

## Docker hub image upload

[Docker hub image upload](https://hostingtutorials.dev/blog/free-spring-boot-host-with-render)

## Deploying on Render

[Deploying on Render
](https://hostingtutorials.dev/blog/free-spring-boot-host-with-render)

## Usage

Visit the hosted application at [https://infospheres.onrender.com](https://infospheres.onrender.com/) to start using Infosphere online.

## Contributing

We welcome contributions! To contribute to Infosphere, follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make changes and commit them.
4. Push the changes to your fork.
5. Submit a pull request.

For major changes, please open an issue first to discuss potential changes.

## License

This project is licensed under the [Apache-2.0 ](https://github.com/Tawfeek01052002/InfoSphere#Apache-2.0-1-ov-file)License - see the [LICENSE]() file for details.

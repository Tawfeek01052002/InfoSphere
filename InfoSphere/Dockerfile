<<<<<<< HEAD
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/SmartContactManagers-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
=======
FROM openjdk:11-jre-slim

WORKDIR /app

COPY . /app

EXPOSE 8080

CMD ["java", "-jar", "SmartContactManagers-0.0.1-SNAPSHOT.jar"]
>>>>>>> c73ad1850f7f95f79b6d3a8997258be0110e42cb

# Step 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using Amazon Corretto OpenJDK (Highly Stable)
FROM amazoncorretto:17-alpine
COPY --from=build /target/demowithswiggy-0.0.1-SNAPSHOT.jar demowithswiggy.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demowithswiggy.jar"]
# Step 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using Amazon Corretto
FROM amazoncorretto:17-alpine
# Keela irukura line un target ulla irukura entha .jar file-aaiyum automatic-aa 'app.jar'-nu rename பண்ணிடும்! Name mismatch varadhu.
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
# Spring Boot-ku Render kudukura target port-ah dynamic-aa assign panra command idhu mapla
ENTRYPOINT ["java","-Dserver.port=${PORT:8080}","-jar","app.jar"]
#maven/jdk image
FROM maven:3.9-eclipse-temurin-21 AS build

#set working directory inside container
WORKDIR /app

#copy pom and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

#copy code
COPY src ./src

#build app
RUN mvn clean package -DskipTests

#jre image for run
FROM eclipse-temurin:21-jre-alpine

#set working directory
WORKDIR /app

#copy built jar
COPY --from=build /app/target/*.jar app.jar

#expose spring boot port
EXPOSE 8080

#start app
ENTRYPOINT ["java", "-jar", "app.jar"]
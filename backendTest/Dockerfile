FROM openjdk:17-jdk-alpine

WORKDIR /backendTest-1.0.0

ARG JAR_FILE=target/*.jar

COPY backendTest-1.0.0.jar app.jar

EXPOSE 5000

CMD ["java", "-jar", "backendTest-1.0.0.jar"]
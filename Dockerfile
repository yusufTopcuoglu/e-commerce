FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} product_service.jar
ENTRYPOINT ["java", "-jar", "/product_service.jar"]
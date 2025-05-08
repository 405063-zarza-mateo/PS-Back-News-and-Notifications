FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/*.jar ps-news-app.jar
ENTRYPOINT ["java","-jar","ps-news-app.jar"]
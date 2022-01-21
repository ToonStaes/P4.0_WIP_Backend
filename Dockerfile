FROM openjdk:18-ea-11-jdk-alpine
EXPOSE 8050
ENV MONGODB_HOST=172.104.143.123
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/chatbot-0.0.1-SNAPSHOT.jar chatbot-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","./chatbot-0.0.1-SNAPSHOT.jar"]
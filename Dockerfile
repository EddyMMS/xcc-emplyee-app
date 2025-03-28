#FROM ....
#Install ..
#sudo apt get askflja
#USER
#EXPOSE 8080
#COPY path_to_jar target/managa app.jar
#CMD ... (java -jar -Dspring.active.profiles=prod app.jar)
#... docker build -t spring-app
#... docker run spring-app
#... docker port-foward
#... docker logs
#... postman

FROM openjdk:21-jdk-slim

WORKDIR /management-app

COPY target/management-app-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

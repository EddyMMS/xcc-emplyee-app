FROM ....

Install ..
sudo apt get askflja

USER

EXPOSE 8080

COPY path_to_jar target/managa app.jar

CMD ... (java -jar -Dspring.active.profiles=prod app.jar)


... docker build -t spring-app
... docker run spring-app
... docker port-foward
... docker logs
... postman
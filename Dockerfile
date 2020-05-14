FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./build/libs/ServiceBusinessStatistics-1.0.0-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "./app.jar"]
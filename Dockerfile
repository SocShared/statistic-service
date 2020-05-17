FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./build/libs/service-business-statistic-1.0.0-SNAPSHOT.jar ./app.jar

EXPOSE 8884

CMD ["java", "-jar", "./app.jar"]
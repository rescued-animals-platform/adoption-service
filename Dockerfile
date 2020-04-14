FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

MAINTAINER Luisa Emme "emmeblm@gmail.com"
ENV LANG C.UTF-8
EXPOSE 8080
RUN echo | ls build/libs
COPY build/libs/*.jar /api/AdoptionService.jar
RUN ls /api
WORKDIR /api

CMD java -jar -Dspring.profiles.active=docker -server -Xms256m -Xmx512m -XX:MaxMetaspaceSize=512m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -XX:+UseTLAB AdoptionService.jar
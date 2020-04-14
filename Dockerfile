FROM gradle:6.3.0-jdk11 AS builder

COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon


FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine AS live

MAINTAINER Luisa Emme "emmeblm@gmail.com"
ENV LANG C.UTF-8
EXPOSE 8080
COPY /app/build/libs/*.jar /api/AdoptionService.jar
RUN ls /api
WORKDIR /api

CMD java -jar -Dspring.profiles.active=live -d64 -server -Xms256m -Xmx512m -XX:MaxMetaspaceSize=512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -XX:+UseTLAB AdoptionService.jar
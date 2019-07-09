FROM adoptopenjdk/openjdk8:jre8u212-b04-alpine

MAINTAINER Luisa Emme "emmeblm@gmail.com"

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

EXPOSE 8080

COPY build/libs/*.jar /api/AdoptionService.jar

RUN ls /api

WORKDIR /api

CMD java -jar -Dspring.profiles.active=$SPRING_PROFILE -d64 -server -Xms256m -Xmx512m -XX:MaxMetaspaceSize=512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -XX:+UseTLAB AdoptionService.jar
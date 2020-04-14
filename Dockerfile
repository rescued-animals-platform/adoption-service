FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-alpine AS builder
COPY . /app
WORKDIR /app
RUN ./gradlew bootJar --no-daemon


FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine AS live
MAINTAINER Luisa Emme emmeblm@gmail.com
ENV LANG C.UTF-8
EXPOSE 8080
COPY --from=builder /app/build/libs/*.jar /api/AdoptionService.jar
WORKDIR /api
CMD java -jar -Dspring.profiles.active=live -server -Xms256m -Xmx512m -XX:MaxMetaspaceSize=512m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC -XX:+UseTLAB AdoptionService.jar
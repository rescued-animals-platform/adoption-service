FROM openjdk:8u171-jdk

MAINTAINER Luisa Emme "emmeblm@hotmail.com"

WORKDIR /usr/lib

COPY gradle/wrapper ./gradle/wrapper
COPY gradlew ./

ENV PATH "${PATH}:/usr/lib"
RUN ./gradlew --version

WORKDIR /usr/src/app

ENTRYPOINT ["gradlew"]
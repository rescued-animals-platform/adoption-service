FROM openjdk:8u171-jdk

MAINTAINER Luisa Emme "emmeblm@hotmail.com"

WORKDIR /usr/lib

COPY gradle/wrapper ./gradle/wrapper
COPY gradlew ./

ENV PATH "${PATH}:/usr/lib"
RUN ./gradlew --version

WORKDIR /usr/src/app


COPY migrations         migrations
COPY src                src
COPY gradle             gradle
COPY build.gradle       build.gradle
COPY settings.gradle    settings.gradle


ENTRYPOINT ["gradlew"]
FROM openjdk:8u171-jdk-slim-stretch

RUN apt-get update;
RUN apt-get install -y wget python;
RUN wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-204.0.0-linux-x86_64.tar.gz
RUN tar -zxvf google-cloud-sdk-204.0.0-linux-x86_64.tar.gz
RUN ./google-cloud-sdk/install.sh -q

ENV PATH "${PATH}:/google-cloud-sdk/bin"

WORKDIR /usr/lib

COPY gradle/wrapper ./gradle/wrapper
COPY gradlew ./

ENV PATH "${PATH}:/usr/lib"
RUN gradlew --version

WORKDIR /usr/src/app

ENV GOOGLE_APPLICATION_CREDENTIALS gcloud-service-key.json

ENTRYPOINT ["scripts/entrypoint.sh"]
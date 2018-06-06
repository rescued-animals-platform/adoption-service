#!/usr/bin/env bash
set -xeuo pipefail

echo "Installing Oracle JDK 8"

apt-get install -y software-properties-common
add-apt-repository "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main"
apt-get update
echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
apt-get install -y oracle-java8-installer
java -version

echo "Gcloud set up"

echo $GCLOUD_SERVICE_KEY > ${HOME}/gcloud-service-key.json;
gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
gcloud config set project $GCLOUD_PROJECT_ID;
apt-get install -y google-cloud-sdk-app-engine-java
export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json

echo "Deploying application"

cd ~/repo
./gradlew appengineDeploy
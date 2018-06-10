#!/usr/bin/env bash
set -xeuo pipefail

echo "Installing gcloud"

sudo wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-204.0.0-linux-x86_64.tar.gz
sudo tar -zxvf google-cloud-sdk-204.0.0-linux-x86_64.tar.gz
sudo ./google-cloud-sdk/install.sh -q

echo "Gcloud set up"

sudo echo $GCLOUD_INTEGRATION_TEST_SERVICE_KEY > ${HOME}/gcloud-service-key.json;
sudo ./google-cloud-sdk/bin/gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json

echo "Configuring postgres host"

sudo sh -c "echo '127.0.0.1      postgres' >> /etc/hosts"

./gradlew clean integrationTest

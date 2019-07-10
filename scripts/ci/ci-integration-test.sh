#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
} &> /dev/null

echo "Running Integration Tests"

source ${HOME}/repo/env.properties
./gradlew integrationTest
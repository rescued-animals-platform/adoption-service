#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
  export ENVIRONMENT_PROPERTIES_PATH=${HOME}/repo/env.properties
} &> /dev/null

echo "Preparing environment"

{
  source ${ENVIRONMENT_PROPERTIES_PATH}
} &> /dev/null

echo "Running Api Tests"

echo $SPRING_PROFILE
echo $ADOPTION_SERVICE_URL
./gradlew apiTest --rerun-tasks
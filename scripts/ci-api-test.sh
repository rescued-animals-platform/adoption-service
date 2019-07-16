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
  export DB_HOST="127.0.0.1:5432/${CI_DB_NAME}"
} &> /dev/null

echo "Running Api Tests"

echo $SPRING_PROFILE
echo $APP_HOST
./gradlew apiTest --rerun-tasks
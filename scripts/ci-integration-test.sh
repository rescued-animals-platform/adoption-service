#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
} &> /dev/null

echo "Preparing environment"

{
  source ${HOME}/repo/env.properties
  export CI_DB_HOST="google/${CI_DB_NAME}?cloudSqlInstance=${CI_DB_CONNECTION_NAME}&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
} &> /dev/null

echo "Running Integration Tests"

echo $SPRING_PROFILE
./gradlew integrationTest
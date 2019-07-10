#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $GCLOUD_CI_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
} &> /dev/null

echo "Setting variable values in app.yaml"

source ${HOME}/repo/env.properties

APP_YAML_DIR=~/repo/src/main/appengine
sed "s/SPRING_PROFILE/${SPRING_PROFILE}/g" ${APP_YAML_DIR}/app.yaml.template > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_CONNECTION_STRING/${DB_CONNECTION_NAME}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_USERNAME/${CI_SQL_CLOUD_MASTER_USERNAME}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_PASSWORD/${CI_SQL_CLOUD_MASTER_PASSWORD}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml
rm ${APP_YAML_DIR}/app.yaml.stashed

echo "Deploying application"

cd ~/repo
./gradlew appengineDeploy

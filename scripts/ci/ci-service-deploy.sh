#!/usr/bin/env bash
set -xeuo pipefail

cd ~/repo

echo "Gcloud auth set up"

{
  echo $GCLOUD_CI_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
} &> /dev/null

echo "Setting active spring profile in app.yaml"

APP_YAML_DIR=~/repo/src/main/appengine
sed "s/SPRING_PROFILE/${SPRING_PROFILE}/g" ${APP_YAML_DIR}/app.yaml.template > ${APP_YAML_DIR}/app.yaml

echo "Deploying application"

cat ~/.profile

source ${HOME}/repo/db.env >> ~/.bashrc
source ${HOME}/repo/db.env >> ~/.profile

echo "DB_CONNECTION_NAME=${DB_CONNECTION_NAME}" >> /etc/environment

hash -r

echo $DB_CONNECTION_NAME

./gradlew appengineDeploy

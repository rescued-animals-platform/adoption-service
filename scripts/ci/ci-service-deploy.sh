#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $GCLOUD_CI_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
} &> /dev/null

echo "Setting active spring profile in app.yaml"

APP_YAML_DIR=~/repo/src/main/appengine
sed "s/SPRING_PROFILE/${SPRING_PROFILE}/g" ${APP_YAML_DIR}/app.yaml.template > ${APP_YAML_DIR}/app.yaml

echo "Deploying application"

cat /etc/environment
sudo source ${HOME}/repo/db.env >> /etc/environment

echo "New etc/environment file"
cat /etc/environment

echo "Deploying application"
cd ~/repo
./gradlew appengineDeploy

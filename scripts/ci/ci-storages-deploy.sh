#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

echo $GCLOUD_CI_SERVICE_KEY > ${HOME}/gcloud-service-key.json;
export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json

echo "Deploying Storages in CI project"

terraform init;
terraform apply -auto-approve -var-file="ci.tfvars";
export DB_CONNECTION_NAME=$(terraform output master_proxy_connection)

APP_YAML_DIR=~/repo/src/main/appengine
sed "s/DB_CONNECTION_STRING/${DB_CONNECTION_NAME}/g" ${APP_YAML_DIR}/app.yaml.template > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_PASSWORD/${CI_SQL_CLOUD_MASTER_PASSWORD}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_USERNAME/${CI_SQL_CLOUD_MASTER_USERNAME}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml
rm ${APP_YAML_DIR}/app.yaml.stashed

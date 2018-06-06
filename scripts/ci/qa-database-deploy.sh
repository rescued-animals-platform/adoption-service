#!/usr/bin/env bash
set -xeuo pipefail

echo "Installing Terraform v0.11.7"

apt-get update;
apt-get install -y unzip wget;
wget https://releases.hashicorp.com/terraform/0.11.7/terraform_0.11.7_linux_amd64.zip;

mkdir -p ~/opt/terraform;
unzip terraform_0.11.7_linux_amd64.zip -d ~/opt/terraform;
cp ~/opt/terraform/terraform /usr/local/bin/;

terraform --version

echo "Gcloud set up"

echo $GCLOUD_SERVICE_KEY > ${HOME}/gcloud-service-key.json;
gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json

echo "Deploying Cloud SQL Postgres database"

cd terraform;
terraform init;
terraform apply -auto-approve;
export CONNECTION_NAME=$(terraform output db_connection_name)

APP_YAML_DIR=~/repo/src/main/appengine
sed "s/DB_CONNECTION_STRING/${CONNECTION_NAME}/g" ${APP_YAML_DIR}/app.yaml.template > ${APP_YAML_DIR}/app.yaml.stashed
sed "s/DB_PASSWORD/${TF_VAR_postgres_password}/g" ${APP_YAML_DIR}/app.yaml.stashed > ${APP_YAML_DIR}/app.yaml
rm ${APP_YAML_DIR}/app.yaml.stashed
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

echo "Setting gcloud authentication"

echo $GCLOUD_SERVICE_KEY > ${HOME}/gcloud-service-key.json;
gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
gcloud config set project $GCLOUD_PROJECT_ID;
export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json

echo "Creating and configuring Cloud SQL Postgres database"

cd terraform;
terraform init;
terraform apply -auto-approve;
export CONNECTION_NAME=$(terraform output db_connection_name)

echo "Deploying application"

cd ~/repo
./gradlew appengineDeploy
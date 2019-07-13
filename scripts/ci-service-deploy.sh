#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json;
#  gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS};
} &> /dev/null

echo "Kubectl set up"

source ${HOME}/repo/env.properties
gcloud container clusters get-credentials ${CI_CLUSTER_NAME} --zone=${CI_CLUSTER_ZONE} --project=${CI_CLUSTER_PROJECT}

{
  export TF_VAR_cloud_sql_master_user_name=${CI_SQL_CLOUD_MASTER_USERNAME}
  export TF_VAR_cloud_sql_master_user_password=${CI_SQL_CLOUD_MASTER_PASSWORD}
  echo $CI_ADOPTION_SERVICE_SERVICE_KEY >> ${HOME}/repo/terraform/deployment/credentials.json;
} &> /dev/null

echo "Deploying service dependencies with terraform"

cd terraform/deployment
terraform init -backend-config="../backend-ci.tfvars"
terraform apply -auto-approve -var-file="../ci.tfvars" -var-file="../backend-ci.tfvars"

echo "Deploying application"

cd ~/repo
kubectl apply -f k8/deploy.yaml
kubectl apply -f k8/service.yaml

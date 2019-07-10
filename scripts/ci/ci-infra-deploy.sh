#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
} &> /dev/null

echo "Deploying Infra in CI project"

{
  export TF_VAR_cloud_sql_master_user_name=${CI_SQL_CLOUD_MASTER_USERNAME}
  export TF_VAR_cloud_sql_master_user_password=${CI_SQL_CLOUD_MASTER_PASSWORD}
} &> /dev/null

cd terraform
terraform init -backend-config="backend-ci.tfvars"
terraform apply -auto-approve -var-file="ci.tfvars"

{
  export ENVIRONMENT_PROPERTIES_PATH=${HOME}/repo/env.properties
  echo "export DB_CONNECTION_NAME=$(terraform output db_connection_name)" >> ${ENVIRONMENT_PROPERTIES_PATH}
  echo "export DB_NAME=$(terraform output db_name)" >> ${ENVIRONMENT_PROPERTIES_PATH}
  echo "export ANIMAL_PICTURES_BUCKET=$(terraform output animal_pictures_bucket)" >> ${ENVIRONMENT_PROPERTIES_PATH}
} &> /dev/null
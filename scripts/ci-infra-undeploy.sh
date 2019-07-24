#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json;
} &> /dev/null

echo "Un deploying Infra in CI project"

{
  export TF_VAR_cloud_sql_master_user_name=${CI_SQL_CLOUD_MASTER_USERNAME}
  export TF_VAR_cloud_sql_master_user_password=${CI_SQL_CLOUD_MASTER_PASSWORD}
} &> /dev/null

cd terraform/infrastructure
terraform init -backend-config="backend-ci.tfvars"
terraform destroy -auto-approve -var-file="ci.tfvars"
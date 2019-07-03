#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $GCLOUD_CI_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
} &> /dev/null

echo "Deploying Storages in CI project"

{
  export TF_VAR_cloud_sql_master_user_name=${CI_SQL_CLOUD_MASTER_USERNAME}
  export TF_VAR_cloud_sql_master_user_password=${CI_SQL_CLOUD_MASTER_PASSWORD}
  export TF_VAR_project=${CI_GCLOUD_PROJECT_ID}
} &> /dev/null

cd terraform;
terraform init;
terraform apply -auto-approve
echo "export DB_CONNECTION_NAME=$(terraform output db_connection_name)" >> ${HOME}/repo/db.env
cat ${HOME}/repo/db.env
#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json;
  gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS};
  export ENVIRONMENT_PROPERTIES_PATH=${HOME}/repo/env.properties
} &> /dev/null

echo "Kubectl set up"

{
  export TF_VAR_cloud_sql_master_user_name=${CI_SQL_CLOUD_MASTER_USERNAME}
  export TF_VAR_cloud_sql_master_user_password=${CI_SQL_CLOUD_MASTER_PASSWORD}
  export TF_VAR_adoption_service_image_tag=${CIRCLE_SHA1}
  echo $CI_ADOPTION_SERVICE_SERVICE_KEY >> ${HOME}/repo/terraform/deployment/secrets/credentials.json;

  source ${ENVIRONMENT_PROPERTIES_PATH}
  gcloud container clusters get-credentials ${CI_CLUSTER_NAME} --zone=${CI_CLUSTER_ZONE} --project=${CI_CLUSTER_PROJECT}
} &> /dev/null

echo "Un deploying service with terraform"

cd terraform/deployment
terraform init -backend-config="backend-ci.tfvars"
terraform destroy -auto-approve
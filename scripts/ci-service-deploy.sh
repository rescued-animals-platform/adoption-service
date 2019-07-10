#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $CI_GCLOUD_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;
} &> /dev/null

echo "Kubectl auth set up"

{
  source ${HOME}/repo/env.properties
  echo $CI_SQL_CLOUD_MASTER_USERNAME >> ${HOME}/ci-sql-cloud-master-username;
  echo $CI_SQL_CLOUD_MASTER_PASSWORD >> ${HOME}/ci-sql-cloud-master-password;
  echo $CI_DB_CONNECTION_NAME >> ${HOME}/ci-db-connection-name;
  echo $CI_DB_NAME >> ${HOME}/ci-db-name;
  echo $CI_ANIMAL_PICTURES_BUCKET >> ${HOME}/ci-animal-pictures-bucket;

  gcloud container clusters get-credentials ${CI_CLUSTER_NAME} --zone=${CI_CLUSTER_ZONE} --project=${CI_CLUSTER_PROJECT}

  kubectl create secret generic adoption-service-secrets \
    --from-file=${HOME}/ci-sql-cloud-master-username \
    --from-file=${HOME}/ci-sql-cloud-master-password \
    --from-file=${HOME}/ci-db-connection-name \
    --from-file=${HOME}/ci-db-name \
    --from-file=${HOME}/ci-animal-pictures-bucket
} &> /dev/null

echo "Deploying application"

cd ~/repo
kubectl apply -f k8/deploy.yaml
kubectl apply -f k8/service.yaml

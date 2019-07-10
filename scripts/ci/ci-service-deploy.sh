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
  gcloud container clusters get-credentials ${CI_CLUSTER_NAME}
} &> /dev/null

echo "Deploying application"

cd ~/repo
kubectl apply -f k8/deploy.yaml
kubectl apply -f k8/service.yaml

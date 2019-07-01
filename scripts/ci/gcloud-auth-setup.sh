#!/usr/bin/env bash
set -xeuo pipefail

echo "Gcloud auth set up"

{
  echo $GCLOUD_CI_SERVICE_KEY >> ${HOME}/gcloud-service-key.json;
  export GOOGLE_APPLICATION_CREDENTIALS=${HOME}/gcloud-service-key.json
} &> /dev/null
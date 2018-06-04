#!/usr/bin/env bash
set -xeuo pipefail

echo $GCLOUD_SERVICE_KEY > ${HOME}/gcloud-service-key.json;

/opt/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;

/opt/google-cloud-sdk/bin/gcloud config set project $GCLOUD_PROJECT_ID;
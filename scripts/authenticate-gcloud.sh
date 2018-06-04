#!/usr/bin/env bash
set -xeuo pipefail

echo $GCLOUD_SERVICE_KEY > ${HOME}/gcloud-service-key.json;

gcloud --version

gcloud auth activate-service-account --key-file=${HOME}/gcloud-service-key.json;

gcloud config set project $GCLOUD_PROJECT_ID;
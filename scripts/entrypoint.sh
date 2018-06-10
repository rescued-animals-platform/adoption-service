#!/usr/bin/env bash
set -xeuo pipefail

TASK=$1

gcloud auth activate-service-account --key-file=gcloud-service-key.json;
gradlew ${TASK}
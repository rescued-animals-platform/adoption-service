#!/usr/bin/env bash
set -xeuo pipefail

cd terraform;

terraform init;
terraform apply -auto-approve;

export CONNECTION_NAME=$(terraform output db_connection_name)

./gradlew appengineDeploy

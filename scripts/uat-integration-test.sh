#!/usr/bin/env bash
set -xeuo pipefail

echo "Getting settings from heroku"

{
  CLOUDINARY_URL="$(heroku config:get CLOUDINARY_URL -a servicio-adopciones-uat)";
  SPRING_DATASOURCE_USERNAME="$(heroku run -a servicio-adopciones-uat echo \$SPRING_DATASOURCE_USERNAME)";
  SPRING_DATASOURCE_PASSWORD="$(heroku run -a servicio-adopciones-uat echo \$SPRING_DATASOURCE_PASSWORD)";
  SPRING_DATASOURCE_URL="$(heroku run -a servicio-adopciones-uat echo \$SPRING_DATASOURCE_URL)";
#  SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL_TO_FIX:4}"
} &> /dev/null

export CLOUDINARY_URL;
export SPRING_DATASOURCE_URL;
export SPRING_DATASOURCE_USERNAME;
export SPRING_DATASOURCE_PASSWORD;
export SPRING_PROFILE=live;
export ADOPTION_SERVICE_URL=https://servicio-adopciones-uat.herokuapp.com;

./gradlew integrationTest --rerun-tasks
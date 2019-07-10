#!/usr/bin/env bash
set -xeuo pipefail

echo "Waiting for Postgres to be ready"

for i in `seq 1 10`;
do
  nc -z localhost 5432 && echo Success && exit 0
  echo -n .
  sleep 1
done
echo Failed waiting for Postgres && exit 1
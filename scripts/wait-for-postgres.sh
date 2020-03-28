#!/bin/sh
set -xeuo pipefail

host="$1"
shift
cmd="$@"

export PGPASSWORD=admin;

until psql -h "$host" -U "admin" -d "postgres"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - executing command"
exec $cmd


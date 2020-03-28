#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER adoption_service WITH PASSWORD 'adoption_service';
    CREATE DATABASE animal_adoption_db;
    GRANT ALL PRIVILEGES ON DATABASE animal_adoption_db TO adoption_service;
EOSQL
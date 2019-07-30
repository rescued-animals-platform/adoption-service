# ------------------------------------------------------------------------------
# CREATE A RANDOM SUFFIX AND PREPARE RESOURCE NAMES
# ------------------------------------------------------------------------------

resource "random_id" "name" {
  byte_length = 3
}

locals {
  db_instance_name = format("%s-%s", var.db_instance_name_prefix, random_id.name.hex)
}

# ------------------------------------------------------------------------------
# CREATE DATABASE INSTANCE
# ------------------------------------------------------------------------------

resource "google_sql_database_instance" "master" {
  name = local.db_instance_name
  database_version = var.postgres_version
  region = var.region

  settings {
    tier = var.db_machine_type
  }
}

resource "google_sql_database" "database" {
  name = var.db_name
  instance = google_sql_database_instance.master.name
  charset = "UTF8"
  collation = "en_US.UTF8"

  depends_on = [google_sql_user.users]
}

resource "google_sql_user" "users" {
  instance = google_sql_database_instance.master.name
  name = var.cloud_sql_master_user_name
  password = var.cloud_sql_master_user_password
}

# ------------------------------------------------------------------------------
# CREATE BUCKET FOR ANIMAL PICTURES STORAGE
# ------------------------------------------------------------------------------

resource "google_storage_bucket" "animal_pictures_bucket" {
  name = var.animal_pictures_storage_bucket
  location = var.region
  force_destroy = true
  storage_class = var.animal_pictures_storage_class
}
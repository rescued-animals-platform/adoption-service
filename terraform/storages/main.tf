# ------------------------------------------------------------------------------
# CREATE A RANDOM SUFFIX AND PREPARE RESOURCE NAMES
# ------------------------------------------------------------------------------

resource "random_id" "name" {
  byte_length = 3
}

locals {
  # If name_override is specified, use that - otherwise use the name_prefix with a random string
  instance_name = var.name_override == null ? format("%s-%s", var.name_prefix, random_id.name.hex) : var.name_override
}

# ------------------------------------------------------------------------------
# CREATE DATABASE INSTANCE
# ------------------------------------------------------------------------------

resource "google_sql_database_instance" "master" {
  project = var.project
  name = local.instance_name
  database_version = var.postgres_version
  region       = var.region

  settings {
    tier = var.machine_type
  }
}

resource "google_sql_database" "database" {
  project = var.project
  name      = var.db_name
  instance  = google_sql_database_instance.master.name
  charset = "UTF8"
  collation = "en_US.UTF8"
}

resource "google_sql_user" "users" {
  project = var.project
  instance = google_sql_database_instance.master.name
  name     = var.master_user_name
  password = var.master_user_password
}

# ------------------------------------------------------------------------------
# CREATE BUCKET FOR ANIMAL PICTURES STORAGE
# ------------------------------------------------------------------------------

resource "google_storage_bucket" "animal-pictures-store" {
  project = var.project
  name = "animal-pictures-store"
  location = var.region
  force_destroy = true
  storage_class = "REGIONAL"
  lifecycle_rule {
    action {
      type = "Delete"
    }
    condition {
      age = 1
    }
  }
}
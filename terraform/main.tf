provider "google-beta" {
  version = "~> 2.7.0"
  project = var.project
  region = var.region
  zone = var.zone
}

terraform {
  required_version = ">= 0.12"

  backend "gcs" {
    bucket = "adoption-service-terraform-state"
    prefix = "terraform/state"
  }
}

module "storages" {
  source = "./storages"

  master_user_name = var.cloud_sql_master_user_name
  master_user_password = var.cloud_sql_master_user_password
  project = var.project
  region = var.region
}
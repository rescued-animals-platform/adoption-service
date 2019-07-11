provider "google" {
  version = "~> 2.9"
  project = var.project
  region = var.region
  zone = var.zone
}

provider "random" {
  version = "~> 2.1"
}

terraform {
  required_version = ">= 0.12"

  backend "gcs" {}
}

module "storages" {
  source = "./storages"

  region = var.region

  animal_pictures_storage_bucket = var.animal_pictures_storage_bucket
  db_instance_name_prefix = var.db_instance_name_prefix
  db_name = var.db_name
  db_machine_type = var.db_machine_type
  cloud_sql_master_user_name = var.cloud_sql_master_user_name
  cloud_sql_master_user_password = var.cloud_sql_master_user_password
}

module "cluster" {
  source = "./cluster"

  location = var.zone

  cluster_name = var.cluster_name
  cluster_initial_node_count = var.cluster_initial_node_count
  cluster_node_machine_type = var.cluster_node_machine_type
}
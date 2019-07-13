provider "kubernetes" {}

terraform {
  required_version = ">= 0.12"

  backend "gcs" {}
}

data "terraform_remote_state" "infra_terraform_state" {
  backend = "gcs"
  config = {
    bucket  = var.bucket
    prefix  = var.prefix
  }
}

resource "kubernetes_secret" "adoption-service-secrets" {
  metadata {
    name = "adoption-service-secrets"
  }

  data = {
    "sql-cloud-master-username" = var.cloud_sql_master_user_name
    "sql-cloud-master-password" = var.cloud_sql_master_user_password
    "animal-pictures-storage-bucket" = var.animal_pictures_storage_bucket
    "db-connection-name" = data.terraform_remote_state.infra_terraform_state.db-connection-name
  }
}

resource "kubernetes_secret" "cloudsql-instance-credentials" {
  metadata {
    name = "cloudsql-instance-credentials"
  }

  data = {
    "credentials.json" = file("${path.module}/credentials.json")
  }
}
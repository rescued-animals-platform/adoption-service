provider "kubernetes" {}

terraform {
  required_version = ">= 0.12"

  backend "gcs" {}
}

module "secrets" {
  source = "./secrets"

  cloud_sql_master_user_name = var.cloud_sql_master_user_name
  cloud_sql_master_user_password = var.cloud_sql_master_user_password
}

module "service" {
  source = "./service"

  adoption_service_resources_limits_cpu = var.adoption_service_resources_limits_cpu
  adoption_service_resources_limits_memory = var.adoption_service_resources_limits_memory
  adoption_service_resources_requests_cpu = var.adoption_service_resources_requests_cpu
  adoption_service_resources_requests_memory = var.adoption_service_resources_requests_memory
  cloudsql_proxy_resources_limits_cpu = var.cloudsql_proxy_resources_limits_cpu
  cloudsql_proxy_resources_limits_memory = var.cloudsql_proxy_resources_limits_memory
  cloudsql_proxy_resources_requests_cpu = var.cloudsql_proxy_resources_requests_cpu
  cloudsql_proxy_resources_requests_memory = var.cloudsql_proxy_resources_requests_memory
  db_host = var.db_host
  infra_state_bucket = var.infra_state_bucket
  infra_state_prefix = var.infra_state_prefix
  min_ready_seconds = var.min_ready_seconds
  number_of_replicas = var.number_of_replicas
  spring_profile = var.spring_profile
  adoption_service_image = var.adoption_service_image
}
# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "infra_state_bucket" {
  description = "Terraform state bucket for the infrastructure data"
  type = string
}

variable "infra_state_prefix" {
  description = "Terraform state prefix for the infrastructure data"
  type = string
}

variable "spring_profile" {
  description = "The spring profile defining the environment where this service will be deployed"
  type = string
}

variable "adoption_service_image_name" {
  description = "The image name for the adoption service pod"
  type = string
}

variable "adoption_service_image_tag" {
  description = "The image tag for the adoption service pod. Provided by the env variable TF_VAR_adoption_service_image_tag in the service deployment script"
  type = string
}

variable "number_of_replicas" {
  description = "The number of desired replicas"
  type = number
}

variable "min_ready_seconds" {
  description = "Minimum number of seconds for which a newly created pod should be ready without any of its container crashing, for it to be considered available"
  type = number
}

variable "db_host" {
  description = "The database host for this service"
  type = string
}

variable "adoption_service_resources_limits_cpu" {
  description = "Describes the maximum amount of CPU allowed"
  type = string
}

variable "adoption_service_resources_limits_memory" {
  description = "Describes the maximum amount of memory allowed"
  type = string
}

variable "adoption_service_resources_requests_cpu" {
  description = "Describes the minimum amount of CPU required."
  type = string
}

variable "adoption_service_resources_requests_memory" {
  description = "Describes the minimum amount of memory required."
  type = string
}

variable "cloudsql_proxy_resources_limits_cpu" {
  description = "Describes the maximum amount of CPU allowed"
  type = string
}

variable "cloudsql_proxy_resources_limits_memory" {
  description = "Describes the maximum amount of memory allowed"
  type = string
}

variable "cloudsql_proxy_resources_requests_cpu" {
  description = "Describes the minimum amount of CPU required."
  type = string
}

variable "cloudsql_proxy_resources_requests_memory" {
  description = "Describes the minimum amount of memory required."
  type = string
}

# ---------------------------------------------------------------------------------------------------------------------
# SENSIBLE PARAMETERS
# This should typically be set as the environment variable TF_VAR_<variable_name> so you don't check it into source control.
# ---------------------------------------------------------------------------------------------------------------------

variable "cloud_sql_master_user_name" {
  description = "The username part for the default user credentials for the could sql instance, i.e. 'master_user_name'@'master_user_host' IDENTIFIED BY 'master_user_password'. This should typically be set as the environment variable TF_VAR_cloud_sql_master_user_name so you don't check it into source control."
  type = string
}

variable "cloud_sql_master_user_password" {
  description = "The password part for the default user credentials for the could sql instance, i.e. 'master_user_name'@'master_user_host' IDENTIFIED BY 'master_user_password'. This should typically be set as the environment variable TF_VAR_cloud_sql_master_user_password so you don't check it into source control."
  type = string
}
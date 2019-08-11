# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "infra_state_bucket" {
  type = string
}

variable "infra_state_prefix" {
  type = string
}

variable "spring_profile" {
  type = string
}

variable "adoption_service_image_name" {
  type = string
}

variable "adoption_service_image_tag" {
  type = string
}

variable "number_of_replicas" {
  type = number
}

variable "min_ready_seconds" {
  type = number
}

variable "db_host" {
  type = string
}

variable "adoption_service_resources_limits_cpu" {
  type = string
}

variable "adoption_service_resources_limits_memory" {
  type = string
}

variable "adoption_service_resources_requests_cpu" {
  type = string
}

variable "adoption_service_resources_requests_memory" {
  type = string
}

variable "cloudsql_proxy_resources_limits_cpu" {
  type = string
}

variable "cloudsql_proxy_resources_limits_memory" {
  type = string
}

variable "cloudsql_proxy_resources_requests_cpu" {
  type = string
}

variable "cloudsql_proxy_resources_requests_memory" {
  type = string
}
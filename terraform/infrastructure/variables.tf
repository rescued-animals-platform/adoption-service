# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "project" {
  description = "The project ID."
  type = string
}

variable "region" {
  description = "The region."
  type = string
}

variable "zone" {
  description = "The zone."
  type = string
}

variable "animal_pictures_storage_bucket" {
  description = "The bucket name where the animal pictures for the adoption service will be persisted"
  type = string
}

variable "db_instance_name_prefix" {
  description = "The name prefix for the database instance. Will be appended with a random string. Use lowercase letters, numbers, and hyphens. Start with a letter."
}

variable "db_name" {
  description = "The name for the database."
  type = string
}

variable "db_machine_type" {
  description = "The machine type to use, see https://cloud.google.com/sql/pricing for more details"
  type = string
}

variable "cluster_name" {
  description = "Cluster name for the GCP Cluster."
  type = string
}

variable "cluster_initial_node_count" {
  description = "Initial node count for the GCP Cluster."
}

variable "cluster_node_machine_type" {
  description = "The machine type to use, see https://cloud.google.com/sql/pricing for more details"
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
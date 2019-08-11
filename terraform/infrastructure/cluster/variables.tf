# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "location" {
  type = string
}

variable "cluster_name" {
  type = string
}

variable "cluster_initial_node_count" {
  type = number
}

variable "cluster_node_machine_type" {
  type = string
}

variable "db-connection-name" {
  type = string
}

variable "animal_pictures_storage_bucket" {
  type = string
}

# ---------------------------------------------------------------------------------------------------------------------
# SENSIBLE PARAMETERS
# This should typically be set as the environment variable TF_VAR_<variable_name> so you don't check it into source control.
# ---------------------------------------------------------------------------------------------------------------------

variable "cloud_sql_master_user_name" {
  type = string
}

variable "cloud_sql_master_user_password" {
  type = string
}
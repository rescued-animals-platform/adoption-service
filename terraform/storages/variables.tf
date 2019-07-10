# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "region" {
  type = string
}

variable "animal_pictures_storage_bucket" {
  type = string
}

variable "db_instance_name_prefix" {
  type = string
}

variable "db_name" {
  type = string
}

variable "db_machine_type" {
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

# ---------------------------------------------------------------------------------------------------------------------
# DEFAULT PARAMETERS
# Generally, these values won't need to be changed.
# ---------------------------------------------------------------------------------------------------------------------

variable "postgres_version" {
  description = "The engine version of the database, e.g. `POSTGRES_9_6`. See https://cloud.google.com/sql/docs/db-versions for supported versions."
  type = string
  default = "POSTGRES_9_6"
}

variable "animal_pictures_storage_class" {
  description = "The storage class for the animal_pictures_storage_bucket"
  type = string
  default = "REGIONAL"
}
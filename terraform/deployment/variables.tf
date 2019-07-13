# ---------------------------------------------------------------------------------------------------------------------
# REQUIRED PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

variable "animal_pictures_storage_bucket" {
  type = string
}

# Terraform state data variables
variable "bucket" {
  type = string
}

variable "prefix" {
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
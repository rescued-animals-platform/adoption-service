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
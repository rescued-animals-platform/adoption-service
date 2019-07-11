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

variable "cluster_initial_node_count" {}

variable "cluster_node_machine_type" {
  type = string
}
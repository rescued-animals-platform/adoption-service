# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "db_connection_name" {
  description = "Instance path for db connections"
  value       = module.storages.db_connection_name
}
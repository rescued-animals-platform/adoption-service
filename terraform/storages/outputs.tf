# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "db_connection_name" {
  description = "Instance path for db connections"
  value       = google_sql_database_instance.master.connection_name
}
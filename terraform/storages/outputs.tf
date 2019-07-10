# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "db_connection_name" {
  value = google_sql_database_instance.master.connection_name
}

output "db_name" {
  value = google_sql_database.database.name
}

output "animal_pictures_bucket" {
  value = google_storage_bucket.animal_pictures_bucket.name
}
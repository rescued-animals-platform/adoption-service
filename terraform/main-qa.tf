terraform {
  backend "gcs" {
    bucket  = "adoption-service-qa-terraform-state"
    prefix  = "terraform/state"
  }
}

resource "google_sql_database_instance" "qa" {
  database_version = "POSTGRES_9_6"
  region = "${var.region}"
  project = "${var.project}"

  settings {
    tier = "db-f1-micro"
    availability_type = "ZONAL"
  }
}

resource "google_sql_user" "users" {
  project = "${var.project}"
  name     = "postgres"
  instance = "${google_sql_database_instance.qa.name}"
  password = "${var.postgres_password}"
}

resource "google_sql_database" "adoption-service-db-qa" {
  project = "${var.project}"
  name      = "animal_adoption_db"
  instance  = "${google_sql_database_instance.qa.name}"
  charset   = "UTF8"
  collation = "en_US.UTF8"
}

output "db_connection_name" {
  value = "${google_sql_database_instance.qa.connection_name}"
}

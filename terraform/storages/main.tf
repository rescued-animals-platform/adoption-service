# ------------------------------------------------------------------------------
# CREATE A RANDOM SUFFIX AND PREPARE RESOURCE NAMES
# ------------------------------------------------------------------------------

resource "random_id" "name" {
  byte_length = 2
}

locals {
  # If name_override is specified, use that - otherwise use the name_prefix with a random string
  instance_name = var.name_override == null ? format("%s-%s", var.name_prefix, random_id.name.hex) : var.name_override
  private_network_name = "private-network-${random_id.name.hex}"
  private_ip_name = "private-ip-${random_id.name.hex}"
}

# ------------------------------------------------------------------------------
# CREATE COMPUTE NETWORKS
# ------------------------------------------------------------------------------

# Simple network, auto-creates subnetworks
resource "google_compute_network" "private_network" {
  provider = "google-beta"
  name = local.private_network_name
}

# Reserve global internal address range for the peering
resource "google_compute_global_address" "private_ip_address" {
  provider = "google-beta"
  name = local.private_ip_name
  purpose = "VPC_PEERING"
  address_type = "INTERNAL"
  prefix_length = 16
  network = google_compute_network.private_network.self_link
}

# Establish VPC network peering connection using the reserved address range
resource "google_service_networking_connection" "private_vpc_connection" {
  provider = "google-beta"
  network = google_compute_network.private_network.self_link
  service = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [
    google_compute_global_address.private_ip_address.name]
}

# ------------------------------------------------------------------------------
# CREATE DATABASE INSTANCE WITH PRIVATE IP
# ------------------------------------------------------------------------------

module "postgres" {
  source = "github.com/gruntwork-io/terraform-google-sql.git//modules/cloud-sql?ref=v0.2.0"

  project = var.project
  region = var.region
  name = local.instance_name
  db_name = var.db_name

  engine = var.postgres_version
  machine_type = var.machine_type

  master_user_password = var.master_user_password
  master_user_name = var.master_user_name
  master_user_host = "%"

  private_network = google_compute_network.private_network.self_link

  dependencies = [google_service_networking_connection.private_vpc_connection.network]
}

# ------------------------------------------------------------------------------
# CREATE BUCKET FOR ANIMAL PICTURES STORAGE
# ------------------------------------------------------------------------------

resource "google_storage_bucket" "animal-pictures-store" {
  project = var.project
  name = "animal-pictures-store"
  location = var.region
  force_destroy = true
  storage_class = "REGIONAL"
  lifecycle_rule {
    action {
      type = "Delete"
    }
    condition {
      age = 1
    }
  }
}
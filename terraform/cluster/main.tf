resource "google_container_cluster" "gcp_kubernetes" {
  name = var.cluster_name
  location = var.location
  initial_node_count = var.cluster_initial_node_count

  master_auth {
    username = ""
    password = ""

    client_certificate_config {
      issue_client_certificate = false
    }
  }

  addons_config {
    kubernetes_dashboard {
      disabled = false
    }
  }

  node_config {
    machine_type = var.cluster_node_machine_type
    oauth_scopes = [
      "https://www.googleapis.com/auth/devstorage.read_only",
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
      "https://www.googleapis.com/auth/service.management.readonly",
      "https://www.googleapis.com/auth/servicecontrol",
      "https://www.googleapis.com/auth/trace.append",
      "https://www.googleapis.com/auth/compute",
    ]
  }
}

resource "kubernetes_secret" "adoption-service-secrets" {
  metadata {
    name = "adoption-service-secrets"
  }

  data = {
    "sql-cloud-master-username" = var.cloud_sql_master_user_name
    "sql-cloud-master-password" = var.cloud_sql_master_user_password
    "db-connection-name" = var.db-connection-name
    "animal-pictures-storage-bucket" = var.animal_pictures_storage_bucket
  }
}

resource "kubernetes_secret" "cloudsql-instance-credentials" {
  metadata {
    name = "cloudsql-instance-credentials"
  }

  data = {
    "credentials.json" = file("${HOME}/credentials.json")
  }
}
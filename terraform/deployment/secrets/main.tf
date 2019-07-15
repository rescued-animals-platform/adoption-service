resource "kubernetes_secret" "adoption-service-secrets" {
  metadata {
    name = "adoption-service-secrets"
  }

  data = {
    "sql-cloud-master-username" = var.cloud_sql_master_user_name
    "sql-cloud-master-password" = var.cloud_sql_master_user_password
  }
}

resource "kubernetes_secret" "cloudsql-instance-credentials" {
  metadata {
    name = "cloudsql-instance-credentials"
  }

  data = {
    "credentials.json" = file("${path.module}/credentials.json")
  }
}
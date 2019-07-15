data "terraform_remote_state" "infra_terraform_state" {
  backend = "gcs"
  config = {
    bucket = var.infra_state_bucket
    prefix = var.infra_state_prefix
  }
}

resource "kubernetes_deployment" "adoption-service-deployment" {
  metadata {
    name = "adoption-service-deployment"
  }

  spec {
    replicas = var.number_of_replicas
    min_ready_seconds = var.min_ready_seconds

    selector {
      match_labels = {
        app = "adoption-service"
      }
    }

    template {
      metadata {
        labels = {
          app = "adoption-service"
        }
      }

      spec {
        container {
          image = "emmeblm/adoption-service:latest"
          name = "adoption-service"

          port {
            container_port = "8080"
          }

          resources {
            limits {
              cpu = var.adoption_service_resources_limits_cpu
              memory = var.adoption_service_resources_limits_memory
            }
            requests {
              cpu = var.adoption_service_resources_requests_cpu
              memory = var.cloudsql_proxy_resources_requests_memory
            }
          }

          env {
            name = "SPRING_PROFILE"
            value = var.spring_profile
          }

          env {
            name = "DB_HOST"
            value = concat(var.db_host, "/", data.terraform_remote_state.infra_terraform_state.outputs.db_name)
          }

          env {
            name = "CI_ANIMAL_PICTURES_BUCKET"
            value = data.terraform_remote_state.infra_terraform_state.outputs.animal_pictures_storage_bucket
          }

          env {
            name = "CI_SQL_CLOUD_MASTER_USERNAME"
            value_from {
              secret_key_ref {
                name = "adoption-service-secrets"
                key = "sql-cloud-master-username"
              }
            }
          }

          env {
            name = "CI_SQL_CLOUD_MASTER_PASSWORD"
            value_from {
              secret_key_ref {
                name = "adoption-service-secrets"
                key = "sql-cloud-master-password"
              }
            }
          }
        }

        container {
          name = "cloudsql-proxy"
          image = "gcr.io/cloudsql-docker/gce-proxy:1.14"

          resources {
            limits {
              cpu = var.cloudsql_proxy_resources_limits_cpu
              memory = var.cloudsql_proxy_resources_limits_memory
            }
            requests {
              cpu = var.cloudsql_proxy_resources_requests_cpu
              memory = var.cloudsql_proxy_resources_requests_memory
            }
          }

          env {
            name = "DB_CONNECTION_NAME"
            value = data.terraform_remote_state.infra_terraform_state.outputs.db_connection_name
          }

          command = [
            "/cloud_sql_proxy",
            "-instances=$(DB_CONNECTION_NAME)=tcp:5432",
            "-credential_file=/secrets/cloudsql/credentials.json"]

          security_context {
            run_as_non_root = true
            allow_privilege_escalation = false
          }

          volume_mount {
            mount_path = "/secrets/cloudsql"
            name = "cloudsql-instance-credentials"
            read_only = true
          }
        }

        volume {
          name = "cloudsql-instance-credentials"
          secret {
            secret_name = "cloudsql-instance-credentials"
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "adoption-service-ingress" {
  metadata {
    name = "adoption-service-ingress"
  }
  spec {
    type = "LoadBalancer"

    port {
      port = "8080"
      target_port = "8080"
    }

    selector = {
      app = "adoption-service"
    }
  }
}
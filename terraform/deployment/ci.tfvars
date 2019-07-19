# ---------------------------------------------------------------------------------------------------------------------
# APPLICATION SPECIFIC VARIABLES
# ---------------------------------------------------------------------------------------------------------------------

infra_state_bucket = "adoption-service-terraform-state-ci"
infra_state_prefix = "terraform/state/infra"
number_of_replicas = 2
min_ready_seconds = 5
spring_profile = "ci"
adoption_service_image = "emmeblm/adoption-service:latest"
db_host = "127.0.0.1:5432"
adoption_service_resources_limits_cpu = "500m"
adoption_service_resources_limits_memory = "1Gi"
adoption_service_resources_requests_cpu = "100m"
adoption_service_resources_requests_memory = "512Mi"
cloudsql_proxy_resources_limits_cpu = "500m"
cloudsql_proxy_resources_limits_memory = "1Gi"
cloudsql_proxy_resources_requests_cpu = "100m"
cloudsql_proxy_resources_requests_memory = "512Mi"

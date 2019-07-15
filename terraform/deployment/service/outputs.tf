# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "adoption_service_endpoint" {
  value = kubernetes_service.adoption-service-ingress.load_balancer_ingress
}
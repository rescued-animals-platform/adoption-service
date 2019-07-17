# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "adoption_service_ip" {
  value = kubernetes_service.adoption-service-ingress.load_balancer_ingress[0].ip
}
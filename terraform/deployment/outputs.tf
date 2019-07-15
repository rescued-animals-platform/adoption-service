# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "adoption_service_endpoint" {
  description = "Endpoint for connections to the deployed adoption service"
  value = module.service.adoption_service_endpoint
}
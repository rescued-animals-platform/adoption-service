# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "adoption_service_ip" {
  description = "Endpoint for connections to the deployed adoption service"
  value = module.service.adoption_service_ip
}
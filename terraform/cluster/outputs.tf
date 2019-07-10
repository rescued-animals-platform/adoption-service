# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "cluster_name" {
  value = google_container_cluster.gcp_kubernetes.name
}
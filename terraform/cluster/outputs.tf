# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "cluster_name" {
  value = google_container_cluster.gcp_kubernetes.name
}

output "cluster_zone" {
  value = google_container_cluster.gcp_kubernetes.zone
}

output "cluster_project" {
  value = google_container_cluster.gcp_kubernetes.project
}
# ------------------------------------------------------------------------------
# MASTER OUTPUTS
# ------------------------------------------------------------------------------

output "db_connection_name" {
  description = "Instance path for db connections"
  value = module.storages.db_connection_name
}

output "db_name" {
  description = "Instance path for db connections"
  value = module.storages.db_name
}

output "animal_pictures_storage_bucket" {
  description = "Bucket name where the animal pictures will be stored for the adoption service"
  value = module.storages.animal_pictures_bucket
}

output "cluster_name" {
  description = "Kubernetes cluster name"
  value = module.cluster.cluster_name
}

output "cluster_zone" {
  description = "Kubernetes cluster zone"
  value = module.cluster.cluster_zone
}

output "cluster_project" {
  description = "Kubernetes cluster project"
  value = module.cluster.cluster_project
}
# ---------------------------------------------------------------------------------------------------------------------
# GENERAL PROJECT VARIABLES
# ---------------------------------------------------------------------------------------------------------------------

project = "adoption-service-ci-245106"
region = "us-east1"
zone = "us-east1-b"

# ---------------------------------------------------------------------------------------------------------------------
# APPLICATION SPECIFIC VARIABLES
# ---------------------------------------------------------------------------------------------------------------------

terraform_state_bucket = "adoption-service-terraform-state-ci"
animal_pictures_storage_bucket = "animal-pictures-store-ci"
db_instance_name_prefix = "adoption-service-db-instance-ci"
db_name = "animal-adoption-db-ci"
db_machine_type = "db-f1-micro"
cluster_name = "rescued-animals-platform"
cluster_initial_node_count = 2
cluster_node_machine_type = "n1-standard-1"
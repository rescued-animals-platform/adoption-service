.DEFAULT_GOAL := help

docker_compose_builder := docker-compose run adoption-service-builder

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-25s\033[0m %s\n", $$1, $$2}'

.PHONY: deploy
deploy: ## Deploys the application and it's dependencies (db container and wiremock container) with docker-compose
	./gradlew clean bootJar --rerun-tasks
	@docker-compose build adoption-service
	@docker-compose up -d --scale adoption-service-builder=0

.PHONY: deploy-dependencies-only
deploy-dependencies-only: ## Deploys only the application's dependencies (db container and wiremock container) with docker-compose
	@docker-compose up -d adoption-service-db wiremock

.PHONY: redeploy-adoption-service
redeploy-adoption-service: ## Stops and re-deploys the application and it's dependencies (db container and wiremock container) with docker-compose
	@docker-compose rm --stop -v --force adoption-service
	make deploy

.PHONY: clean
clean: ## Cleans up all the docker containers using "docker-compose down"
	@docker-compose down --remove-orphans -v

.PHONY: test-unit
test-unit: ## Builds the code and runs unit tests
	./gradlew clean build --rerun-tasks

.PHONY: pitest
pitest: ## Executes mutation testing coverage analysis
	./gradlew pitest --rerun-tasks

.PHONY: sonar
sonar: ## Executes sonarqube static analysis
	./gradlew build sonarqube --rerun-tasks

.PHONY: test-integration
test-integration: deploy-dependencies-only ## Runs integration tests from the builder container inside the docker-compose network
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make clean

.PHONY: test-api
test-api: deploy ## Runs api tests from the builder container inside the docker-compose network
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make clean

.PHONY: test-arch-unit
test-arch-unit: ## Runs architecture tests (atomic fitness functions) written with arch-unit
	./gradlew archUnitTest --rerun-tasks

.PHONY: test-all
test-all: test-unit test-arch-unit pitest deploy ## Runs all tests (unit, integration, api, architecture) and cleans the environment after a successful result
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make clean
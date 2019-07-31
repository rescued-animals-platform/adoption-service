docker_compose_builder = docker-compose run --rm adoption-service-builder

package:
	./gradlew clean bootJar

deploy: package
	@docker-compose build adoption-service
	@docker-compose up -d adoption-service adoption-service-db

deploy-adoption-service-db:
	@docker-compose up -d adoption-service-db

undeploy:
	@docker-compose down

builder-build:
	@docker-compose build adoption-service-builder

unit-test:
	./gradlew check --rerun-tasks

pitest:
	./gradlew pitest

style-check:
	./gradlew pmdMain spotbugsMain pmdTest pmdIntegrationTest pmdApiTest --rerun-tasks

integration-test: deploy-adoption-service-db builder-build
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make undeploy

api-test: deploy builder-build
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make undeploy

all-test: unit-test pitest deploy builder-build
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make undeploy
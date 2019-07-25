workspace=$(shell pwd)

postgres_db = animal-adoption-db
spring_profile = $(SPRING_PROFILE)
docker_compose_builder = docker-compose run --rm adoption-service-builder

package:
	./gradlew clean bootJar

deploy: package
	@docker-compose build adoption-service
	@docker-compose up -d adoption-service adoption-service-db; sleep 10;

deploy-adoption-service-db:
	@docker-compose up -d adoption-service-db

undeploy:
	@docker-compose stop

builder-build:
	@docker-compose build adoption-service-builder

unit-test:
	./gradlew check --rerun-tasks

pitest:
	./gradlew pitest

pmd-check:
	./gradlew pmdMain pmdTest pmdIntegrationTest pmdApiTest --rerun-tasks

integration-test: deploy-adoption-service-db builder-build
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make undeploy

api-test: deploy builder-build
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make undeploy

all-test: unit-test deploy builder-build
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make undeploy
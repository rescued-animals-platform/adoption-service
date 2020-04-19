docker_compose_builder = docker-compose run adoption-service-builder

builder-build:
	@docker-compose build adoption-service-builder

deploy:
	./gradlew clean bootJar --rerun-tasks
	@docker-compose build adoption-service
	docker-compose up -d --scale adoption-service-builder=0

deploy-dependencies-only:
	@docker-compose up -d adoption-service-db wiremock

undeploy:
	@docker-compose down --remove-orphans -v

unit-test:
	./gradlew clean check --rerun-tasks

pitest:
	./gradlew pitest --rerun-tasks

style-check:
	./gradlew pmdMain spotbugsMain pmdTest pmdIntegrationTest pmdApiTest --rerun-tasks

integration-test: builder-build deploy-dependencies-only
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make undeploy

api-test: deploy builder-build
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make undeploy

all-test: unit-test pitest deploy builder-build
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make undeploy
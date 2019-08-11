docker_compose_builder = docker-compose run adoption-service-builder

builder-build:
	@docker-compose build adoption-service-builder

deploy:
	$(docker_compose_builder) bootJar
	@docker-compose build adoption-service
	@docker-compose up -d adoption-service adoption-service-db

deploy-adoption-service-db:
	@docker-compose up -d adoption-service-db

undeploy:
	@docker-compose down

unit-test:
	./gradlew check --rerun-tasks

pitest:
	./gradlew pitest

style-check:
	./gradlew pmdMain spotbugsMain pmdTest pmdIntegrationTest pmdApiTest --rerun-tasks

integration-test: deploy-adoption-service-db
	$(docker_compose_builder) integrationTest --rerun-tasks
	make undeploy

api-test: deploy
	$(docker_compose_builder) apiTest --rerun-tasks
	make undeploy

all-test: unit-test pitest deploy
	$(docker_compose_builder) integrationTest apiTest --rerun-tasks
	make undeploy
docker_compose_builder = docker-compose run adoption-service-builder

builder-build:
	@docker-compose build adoption-service-builder

deploy:
	./gradlew clean bootJar
	@docker-compose build adoption-service
	docker-compose up -d --scale adoption-service-builder=0

deploy-dependencies-only:
	@docker-compose up -d adoption-service-db wiremock

undeploy:
	@docker-compose down --remove-orphans -v

unit-test:
	./gradlew clean check

pitest:
	./gradlew pitest

style-check:
	./gradlew pmdMain spotbugsMain pmdTest pmdIntegrationTest pmdApiTest

integration-test: builder-build deploy-dependencies-only
	$(docker_compose_builder) gradle integrationTest
	make undeploy

api-test: deploy builder-build
	$(docker_compose_builder) gradle apiTest
	make undeploy

all-test: unit-test pitest deploy builder-build
	$(docker_compose_builder) gradle integrationTest apiTest
	make undeploy
docker_compose_builder = docker-compose run adoption-service-builder

builder-build:
	@docker-compose build adoption-service-builder

deploy:
	./gradlew clean bootJar
	@docker-compose build adoption-service
	SPRING_PROFILE=docker; docker-compose up -d

deploy-adoption-service-db:
	@docker-compose up -d adoption-service-db

undeploy:
	@docker-compose down

unit-test:
	./gradlew clean check

pitest:
	./gradlew pitest

style-check:
	./gradlew pmdMain spotbugsMain pmdTest pmdIntegrationTest pmdApiTest

integration-test: deploy-adoption-service-db
	SPRING_PROFILE=docker; $(docker_compose_builder) gradle integrationTest
	make undeploy

api-test: deploy
	$(docker_compose_builder) gradle apiTest
	make undeploy

all-test: unit-test pitest deploy builder-build
	SPRING_PROFILE=docker; $(docker_compose_builder) gradle integrationTest apiTest
	make undeploy
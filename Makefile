docker_compose_builder := docker-compose run adoption-service-builder

.PHONY: deploy
deploy:
	./gradlew clean bootJar --rerun-tasks
	@docker-compose build adoption-service
	@docker-compose up -d --scale adoption-service-builder=0

.PHONY: deploy.dependencies-only
deploy.dependencies-only:
	@docker-compose up -d adoption-service-db wiremock

.PHONY: deploy.redeploy-adoption-service
deploy.redeploy-adoption-service:
	@docker-compose rm --stop -v --force adoption-service
	make deploy

.PHONY: clean
clean:
	@docker-compose down --remove-orphans -v

.PHONY: test.unit
test.unit:
	./gradlew clean build --rerun-tasks

.PHONY: pitest
pitest:
	./gradlew pitest --rerun-tasks

.PHONY: sonar
sonar:
	./gradlew clean build sonarqube --rerun-tasks

.PHONY: test.integration
test.integration: deploy.dependencies-only
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make clean

.PHONY: test.api
test.api: deploy
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make clean

.PHONY: test.all
test.all: test.unit pitest deploy
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make clean
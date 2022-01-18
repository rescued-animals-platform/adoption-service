docker_compose_builder = docker-compose run adoption-service-builder

deploy:
	./gradlew clean bootJar --rerun-tasks
	@docker-compose build adoption-service
	docker-compose up -d --scale adoption-service-builder=0

deploy-dependencies-only:
	@docker-compose up -d adoption-service-db wiremock

redeploy-adoption-service:
	@docker-compose rm --stop -v --force adoption-service
	make deploy

undeploy:
	@docker-compose down --remove-orphans -v

unit-test:
	./gradlew clean build --rerun-tasks

pitest:
	./gradlew pitest --rerun-tasks

integration-test: deploy-dependencies-only
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make undeploy

api-test: deploy
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle apiTest --rerun-tasks
	make undeploy

all-test: unit-test pitest deploy
	@docker-compose build adoption-service-builder
	$(docker_compose_builder) gradle integrationTest apiTest --rerun-tasks
	make undeploy
workspace=$(shell pwd)

postgres_db = animal-adoption-db
spring_profile = $(SPRING_PROFILE)
docker_compose_builder = docker-compose run --rm adoption-service-builder

package:
	./gradlew clean bootJar

deploy: package
	@docker-compose build;
	@docker-compose up -d adoption-service adoption-service-db; sleep 10;

undeploy:
	@docker-compose stop

unit-test:
	./gradlew clean build

pitest:
	./gradlew pitest

integration-test:
	@docker-compose start adoption-service-db
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	make undeploy

api-test: deploy
	./gradlew apiTest --rerun-tasks
	make undeploy

all-test: unit-test deploy
	$(docker_compose_builder) gradle integrationTest --rerun-tasks
	./gradlew apiTest --rerun-tasks
	make undeploy
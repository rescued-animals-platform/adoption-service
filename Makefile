workspace=$(shell pwd)

postgres_db = animal-adoption-db
spring_profile = $(SPRING_PROFILE)

package:
	./gradlew clean bootJar

deploy: package
	@docker-compose build;
	@docker-compose up -d; sleep 10;

undeploy:
	@docker-compose stop

deploy-postgres:
	@if ! docker ps | grep -e adoption-service-db$ ; then echo "Deploying Adoption Service DB"; docker run --rm --name adoption-service-db -d -p 5432:5432 -e POSTGRES_DB=$(postgres_db) postgres:10; sleep 2; fi

undeploy-postgres:
	@docker kill adoption-service-db

build-project:
	./gradlew clean build

unit-test:
	./gradlew cleanTest test

pitest:
	./gradlew pitest

integration-test: deploy-postgres
	./gradlew integrationTest --rerun-tasks
	make undeploy-postgres

api-test: deploy
	./gradlew apiTest --rerun-tasks
	make undeploy

all-test: build-project pitest integration-test api-test
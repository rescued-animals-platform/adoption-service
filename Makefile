workspace=$(shell pwd)

postgres_db = animal_adoption_db
spring_profile = $(SPRING_PROFILE)

package:
	./gradlew clean bootJar

deploy: deploy-postgres package
	@docker build -t adoption-service .
	@docker run -d --rm --name adoption-service -p 8080:8080 --link adoption-service-db:adoption-service-db -e SPRING_PROFILE=$(spring_profile) adoption-service;

undeploy: undeploy-postgres
	@docker kill adoption-service

deploy-postgres:
	@if ! docker ps | grep adoption-service-db ; then echo "Deploying Adoption Service DB"; docker run --rm --name adoption-service-db -d -p 5432:5432 -e POSTGRES_DB=$(postgres_db) postgres:10; sleep 2;fi

undeploy-postgres:
	@docker kill adoption-service-db

build-project:
	./gradlew clean build

unit-test:
	./gradlew clean test

pitest:
	./gradlew pitest

integration-test: deploy-postgres
	./gradlew integrationTest

api-test: deploy-postgres
	./gradlew apiTest

all-test: build-project pitest integration-test api-test

workspace=$(shell pwd)

postgres_db = animal_adoption_db

deploy-postgres:
	@if ! docker ps | grep postgres ; then echo "Deploying postgres"; docker run --rm --name postgres -d -p 5432:5432 -e POSTGRES_DB=$(postgres_db) postgres:10; sleep 2;fi

undeploy-postgres:
	@docker kill postgres

build-project:
	./gradlew clean build

unit-test:
	./gradlew test

pitest:
	./gradlew pitest

integration-test: deploy-postgres
	./gradlew integrationTest
	make undeploy-postgres

api-test: deploy-postgres
	./gradlew apiTest
	make undeploy-postgres

all-test:
	./gradlew clean build
	./gradlew pitest
	make deploy-postgres
	./gradlew integrationTest
	./gradlew apiTest
	make undeploy-postgres

postgres_db ?= animal_adoption_db

deploy-postgres:
	@if ! docker ps | grep postgres ; then echo "Deploying postgres"; docker run --rm --name postgres -d -p 5432:5432 -e POSTGRES_DB=$(postgres_db) postgres:10 ; sleep 2;fi

undeploy-postgres:
	@docker kill postgres

build-project:
	./gradlew clean build

unit-test:
	./gradlew clean test

integration-test: deploy-postgres
	./gradlew clean integrationTest
	make undeploy-postgres

pitest:
	./gradlew clean pitest

all-test: unit-test pitest integration-test

workspace=$(shell pwd)

postgres_db = animal_adoption_db
toolset-image = adoption-service-toolset-image


build-toolset:
	@docker build -t $(toolset-image) .

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
	docker run --rm --name adoption-service-toolset --link postgres:postgres -v ~/.gradle/caches:/root/.gradle/caches:rw -v $(workspace):/usr/src/app:rw -p 8080:8080 $(toolset-image) integrationTest
	make undeploy-postgres

integration-test-dev: deploy-postgres
	./gradlew integrationTest -PspringProfiles=dev
	make undeploy-postgres

all-test:
	./gradlew clean build
	make pitest integration-test-dev
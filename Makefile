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

unit-test:
	./gradlew clean build

pitest:
	./gradlew pitest

integration-test:
	@docker-compose up -d adoption-service-db
	./gradlew integrationTest --rerun-tasks
	make undeploy

api-test: deploy
	./gradlew apiTest --rerun-tasks
	make undeploy

all-test: unit-test deploy
	./gradlew integrationTest --rerun-tasks
	./gradlew apiTest --rerun-tasks
	make undeploy
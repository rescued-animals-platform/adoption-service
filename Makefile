deploy-compose:
	@docker-compose up --build -d

undeploy-compose:
	@docker-compose down

build-compose:
	@docker-compose build

build-project:
	./gradlew clean build

unit-test:
	./gradlew clean test

integration-test:
	@docker-compose run --rm gradlew-builder tasks
	make undeploy-compose

pitest:
	./gradlew clean pitest

all-test: unit-test pitest integration-test

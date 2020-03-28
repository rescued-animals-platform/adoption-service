# Animal adoption service

[![CircleCI](https://circleci.com/gh/rescued-animals-platform/adoption-service.svg?style=shield)](https://circleci.com/gh/rescued-animals-platform/adoption-service)


Tech stack: Java 8, Gradle 6.3, SpringBoot 2.2.6.RELEASE, Flyway, Docker Engine v19.03.8, PostgresSQL 10.6, Terraform v0.12.4, Google Cloud Platform.

# Setting up your development environment (Linux, macOS)

Follow the next steps in the order they appear. After completing them, you would be able to start contributing to the project. Good luck and thanks!

- Make sure you have installed the following:
    - Java 8
    - Docker
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login

- Set the following environment variables to be active in your project root directory:

        export SPRING_PROFILE=local
        export ADOPTION_SERVICE_URL=http://localhost:8080 (needed only if you want to execute the application from your IDE)
 
- Run: `make all-test` to run all tests and verify everything works as expected.

## [Important] If you want to run integration tests from the IDE

Without this steps, you would be able to run all tests from a terminal. Only if you want to be able to run integration tests from your IDE, you can do the following:
        
- Setup your preferred IDE to run tests with Gradle (instead of JUnit) and add the environment variable `SPRING_PROFILE=local` to it. For example, add the env variable to the Gradle Template in the running configurations of IntelliJ IDEA.

_**(Always, before running the integration tests)**_

- Verify that the adoption-service-db container is running:

        docker ps -a    
  
  If it is not, run: `make deploy-adoption-service-db`
  
## [Important] If you want to run api tests from the IDE

Without this steps, you would be able to run all tests from a terminal. Only if you want to be able to run api tests from your IDE, you can do the following:

_**(Only for the first time)**_
        
- Setup your preferred IDE to run tests with Gradle (instead of JUnit) and add the environment variable `ADOPTION_SERVICE_URL=http://localhost:8080` and `SPRING_PROFILE=local` to it. For example, add the env variable to the Gradle Template in the running configurations of IntelliJ IDEA.

_**(Always, before running the api tests)**_

- Deploy the application with:

        make deploy
        
## Useful commands

1. Run unit test with:
        
        make unit-test

2. Run pitest (mutation testing) with:
    
        make pitest
        
3. Check code style with:
        
        make style-check
        
   Task unit-test also checks the code style by default before running the unit tests.

3. Run integration tests with:

        make integration-test

4. Run api tests with:

        make api-test

5. Run all tests (unit, integration, and api) with:

        make all-test

6. Deploy the adoption service database (useful when running integration tests from the IDE) with:

        make deploy-adoption-service-db
        
7. Local deployment of the adoption service (useful when running api tests from the IDE and for manual local testing):

        make deploy
        
   Un deploy with `make undeploy`
        

Check the **Makefile** file to discover more tasks.

# Postman collection with sample requests

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3916ba8b54f6943cb99b)
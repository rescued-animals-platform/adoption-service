# Animal adoption service

[![CircleCI](https://circleci.com/gh/rescued-animals-platform/adoption-service.svg?style=shield)](https://circleci.com/gh/rescued-animals-platform/adoption-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=rescued-animals-platform_adoption-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=rescued-animals-platform_adoption-service)

Tech stack: Java 11, Gradle 7.3.3, SpringBoot 2.2.6.RELEASE, Flyway, Docker Engine v19.03.8, PostgresSQL 10.6, Heroku.

# Setting up your development environment (Linux, macOS)

Follow the next steps in the order they appear. After completing them, you would be able to start contributing to the project. Good luck and thanks!

- Make sure you have installed the following:
    - Java 11
    - Docker
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login
 
- Run: `make all-test` to run all tests and verify everything works as expected.

## [Important] If you want to run integration tests from the IDE

Without these steps, you would be able to run all tests from a terminal. Only if you want to be able to run integration tests from your IDE, you can do the following:
        
- Setup your preferred IDE to run tests with Gradle (instead of JUnit) and add the environment variable `SPRING_PROFILE=local` to it. For example, add the env variable to the Gradle Template in the running configurations of IntelliJ IDEA.

_**(Always, before running the integration tests)**_

- Verify that the dependencies (wiremock and adoption-service-db) are running:

        docker ps -a    
  
  If they're not, run: `make deploy-dependencies-only`
  
## [Important] If you want to run api tests from the IDE

Without these steps, you would be able to run all tests from a terminal. Only if you want to be able to run api tests from your IDE, you can do the following:

_**(Only for the first time)**_
        
- Setup your preferred IDE to run tests with Gradle (instead of JUnit) and add the following environment variables to it. (e.g. add the env variable to the Gradle Template in the running configurations of IntelliJ IDEA):

        SPRING_PROFILE=local
        ADOPTION_SERVICE_URL=http://localhost:8080
        JWT_ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNrc2hkYjgydHUzYm5pb3NodWprbnNraGRiYmprc2JvM3UifQ.eyJpc3MiOiJodHRwOi8vd2lyZW1vY2s6ODA4MC8iLCJzdWIiOiIxMjM0NTY3ODkwIiwiYXVkIjoiaHR0cHM6Ly9hbmltYWxlcy1yZXNjYXRhZG9zL3NlcnZpY2lvLWFkb3BjaW9uZXMuZWMiLCJpYXQiOjE1ODY2MzcwNDIsImV4cCI6OTA4NjcyMzQ0MiwiaHR0cHM6Ly9hZG9wdGlvbi1zZXJ2aWNlL29yZ2FuaXphdGlvbl9pZCI6IjU2MDA5MTE5LTQ0YmQtNDY5YS1hNTliLTQwMWFiMjNkMTljYSIsInBlcm1pc3Npb25zIjpbIm1hbmFnZTphbmltYWxzIiwicmVhZDphbmltYWxzLXB1YmxpYyJdfQ.O2mJvPPIc2NJKd5tfaORRUjXvXxAU6UppPiSuDF9fFZz2ukIZA3zX1GoPOUGyL9GL8zrCgorkMOYcYB5ECj8RkA6U3DGk6kjZ9J-wpGq3BmcrF-xlmYpHzwtgkBRfdmbKGO801TsobsSiWEdSCtenSN0OOzzBk1L3CrdYDOqAw4C5xsOdJ_wj5ILny8xZ4jSY3YymLBz2oApWLnwWiJNXiMQ0kaOJpiKXGbduEy39VPDahe3L2TJGwV1wKc_vpg3IyMxGztL_jK_s2fXHQ94mFzQ9Zo5bwyPK-eDHHZcRSciByky5EIFCpg2fhsKn4uPWFarCLTmCzg3cWQT9NCTvQ

_**(Always, before running the api tests)**_

- Deploy the application in a docker container with `make deploy` or run the application from your IDE (remember to set up the SPRING_PROFILE env variable if running from the IDE and verify dependencies are running).
        
## Useful commands

1. Run unit test with:
        
        make unit-test

2. Run pitest (mutation testing) with:
    
        make pitest
        
3. Check code style with SonarLint.

3. Run integration tests with:

        make integration-test

4. Run api tests with:

        make api-test

5. Run all tests (unit, integration, and api) with:

        make all-test

6. Deploy only the application dependencies (useful when running integration/api tests from the IDE) with:

        make deploy-dependencies-only
        
7. Local deployment of the adoption service (useful when running api tests from the IDE and for manual local testing):

        make deploy
        
   Un-deploy all containers with `make undeploy`
        

Check the **Makefile** file to discover more tasks.

# Postman collection with sample requests

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3916ba8b54f6943cb99b)
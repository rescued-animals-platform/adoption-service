# Animal adoption service

[![CircleCI](https://circleci.com/gh/rescued-animals-platform/adoption-service.svg?style=shield)](https://circleci.com/gh/rescued-animals-platform/adoption-service)


Tech stack: Java 11, Gradle 6.3, SpringBoot 2.2.6.RELEASE, Flyway, Docker Engine v19.03.8, PostgresSQL 10.6, Heroku.

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
        JWT_ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNrc2hkYjgydHUzYm5pb3NodWprbnNraGRiYmprc2JvM3UifQ.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxMjMvIiwic3ViIjoiMTIzNDU2Nzg5MCIsImF1ZCI6Imh0dHBzOi8vYW5pbWFsZXMtcmVzY2F0YWRvcy9zZXJ2aWNpby1hZG9wY2lvbmVzLmVjIiwiaWF0IjoxNTg2NjM3MDQyLCJleHAiOjkwODY3MjM0NDIsInBlcm1pc3Npb25zIjpbInJlYWQ6YW5pbWFscyIsImNyZWF0ZTphbmltYWxzIiwidXBkYXRlOm9yZ2FuaXphdGlvbiJdfQ.bDGWWfgkZ_OqtKzltBfWFe321fRHlkpvBbMDEHy4vGk3U22Eb2SRNHJGxKZNw7oRVfUHPN7TKPFHvn3vr5zOMjptGUWbtXg9O1C9b_KpMfHEOHEV87aW2kpMJct5UY0h_cpKEI2bl10JD2Q49Qo1pnCmlauf4Gekgnb0BkhCaX4GZdlc5x3qQCfk_kaNhhI7_f52eRuACr3kWhR1mYrtKfHPpplm8PauWtG1Vt4AoheMLzqef8pW1gXNwJxUzi6VM2qqATV6j_Yxl4AxenqcV9SYrhn5EjFSypTW0kb_R56p9sc7MCIi_e-RAEZzQIkcFH_Rmj0_Aau88i7XKSpgAg

_**(Always, before running the api tests)**_

- Deploy the application in a docker container with `make deploy` or run the application from your IDE (remember to set up the SPRING_PROFILE env variable if running from the IDE and verify dependencies are running).
        
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

6. Deploy only the application dependencies (useful when running integration/api tests from the IDE) with:

        make deploy-dependencies-only
        
7. Local deployment of the adoption service (useful when running api tests from the IDE and for manual local testing):

        make deploy
        
   Un-deploy all containers with `make undeploy`
        

Check the **Makefile** file to discover more tasks.

# Postman collection with sample requests

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3916ba8b54f6943cb99b)
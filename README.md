# Animal adoption service

Tech stack (local enviroment): Java 8, Gradle 4.0, SpringBoot 2.0.2.RELEASE, Flyway, Jetty 9.4.10.v20180503, Docker 18.03.1-ce, PostgresSQL 10, Google Cloud Storage.

# Configuration for your development environment

- Make sure you have installed the following:
    - Java 8
    - Docker
    - `gcloud` command line tool
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login
        
- Login 
 
- Run: `make all-test` to run all tests and verify everything works as expected.

## Useful commands

1. Run unit test with:
        
        make unit-test
3. Run pitest (mutation testing) with:
    
        make pitest
2. Run integration tests with:

        make integration-test
   This command automatically runs deploy-postgres (before tests) and undeploy-postgres (after tests).
3. Run all tests (unit, pitest, and integration) with:

        make all-test
4. Deploy the postgres database with:

        make deploy-postgres
   
   You'll need to do this if you want to run integration tests from the IDE.
5. Undeploy the postgres database with:

        make undeploy-postgres
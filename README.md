# Animal adoption service

Tech stack: Java 8, Gradle 4.0, SpringBoot 2.0.2.RELEASE, Flyway, Jetty 9.4.10.v20180503, Docker 18.03.1-ce, PostgresSQL 10.

# Configuration for your development environment

- Make sure you have installed the following:
    - Java 8
    - Docker
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login

- Build docker-compose (for integration testing):

        make build-compose
 
- Run: `make all-test` to run all tests and verify everything works as expected.

- **Note: If you want to run integration tests from the IDE you need to configure the following first:**

    1) Verify that the container adoption_service_postgres is running. If it's not then run:
            
            make deploy-compose

    2) Run: 
    
            sudo sh -c "echo '127.0.0.1      adoption_service_postgres' >> /etc/hosts"

       **Now you're ready to run your integration tests from your IDE!**

## Useful commands

1. Run unit test with:
        
        make unit-test
3. Run pitest (mutation testing) with:
    
        make pitest
2. Run integration tests with:

        make integration-test
   This command automatically runs docker-compose up (before tests) and down (after tests).
3. Run all tests (unit, pitest, and integration) with:

        make all-test
4. Deploy the postgres database with:

        make deploy-compose
   
   You'll need to do this if you want to run integration tests from the IDE.
5. Undeploy the postgres database with:

        make undeploy-compose
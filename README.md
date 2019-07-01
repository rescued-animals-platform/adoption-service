# Animal adoption service

[![CircleCI](https://circleci.com/gh/rescued-animals-platform/adoption-service.svg?style=svg)](https://circleci.com/gh/rescued-animals-platform/adoption-service)


Tech stack: Java 8, Gradle 4.0, SpringBoot 2.0.2.RELEASE, Flyway, Jetty 9.4.10.v20180503, Docker 18.03.1-ce, PostgresSQL 10, Google Cloud Platform.

# Setting up your development environment (Linux, macOS)

Follow the next steps in the order they appear. After completing them, you would be able to start contributing to the project. Good luck and thanks!

- Make sure you have installed the following:
    - Java 8
    - Docker
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login

- Add the next line to your bashrc (or the shell you're using) to set your local environment as a dev environment:

        export SPRING_PROFILE=dev
 
- Run: `make all-test` to run all tests and verify everything works as expected.

## [Important] If you want to run integration tests from the IDE

Without this steps, you would be able to run all tests from a terminal. Only if you want to be able to run integration tests from your IDE, you can do the following:

_**(Only for the first time)**_
  
- Run:
        
        sudo sh -c "echo '127.0.0.1      postgres' >> /etc/hosts"
        
- Setup your preferred IDE to run tests with Gradle (instead of JUnit) and add the environment variable `SPRING_PROFILE=dev` to it. For example, add the env variable to the Gradle Template in the running configurations of IntelliJ IDEA.

_**(Always, before running the integration tests)**_

- Verify that the postgres container is running:

        docker ps -a    
  
  If it is not, run: `make deploy-postgres`
        
## Useful commands

1. Run unit test with:
        
        make unit-test

2. Run pitest (mutation testing) with:
    
        make pitest

3. Run integration tests with:

        make integration-test
   
   This command automatically runs deploy-postgres (before tests) and undeploy-postgres (after tests).

4. Run api tests with:

        make api-test
   
   This command automatically runs deploy-postgres (before tests) and undeploy-postgres (after tests).

5. Run all tests (unit, pitest, and integration) with:

        make all-test

6. Deploy the postgres database with:

        make deploy-postgres
   
   You'll need to do this if you want to run integration tests from the IDE.

7. Undeploy the postgres database with:

        make undeploy-postgres
        

Check the **Makefile** file to discover more tasks.

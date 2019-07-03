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
  
 
# [Optional] Setting up your Google Cloud account

If you want to setup a Gcloud account to deploy you need to do the following:

- After creating your Google Cloud Account and creating a project inside it, you'll need to enable the following APIs:
    - Compute Engine API
    - App Engine Admin API
    - Cloud SQL Admin API
    
- Create a service account with the following permissions (functions):
    - App Engine Administrator
    - Cloud SQL Administrator
    - Compute Network Administrator
    - Editor
    - Storage Administrator
    
-  Create a bucket for terraform to save it's state with the name: `adoption-service-terraform-state` and the following configuration:
    - Storage Class: Regional
    - Region: us-east4
    
- Create a new app engine application inside your project

In the machine that will run the terraform scripts to deploy the storages and/or the gradle plugin to deploy the service into app engine, you'll need to setup the following environment variables:
        
        SPRING_PROFILE=ci
        CI_GCLOUD_PROJECT_ID=<your-project-id-in-gcloud>

**Note:** The adoption service can be deployed only in a CI environment for now. It is not production ready yet.  
        
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

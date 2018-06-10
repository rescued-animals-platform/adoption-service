# Animal adoption service

Tech stack: Java 8, Gradle 4.0, SpringBoot 2.0.2.RELEASE, Flyway, Jetty 9.4.10.v20180503, Docker 18.03.1-ce, PostgresSQL 10, Google Cloud Platform.

# Setting up your development environment (Linux, macOS)

Follow the next steps in the order they appear. After completing them, you would be able to start contributing to the project. Good luck and thanks!

- Make sure you have installed the following:
    - Java 8
    - Docker
    
- Login to docker with your credentials. If you don't have an account, create one in the [Docker Hub site](https://hub.docker.com/)

        docker login

- Open [google cloud platform console](https://console.cloud.google.com/) in your browser. If you don't have an account, create one.

- Create a project and inside it, go to [Storage](https://console.cloud.google.com/storage) and create a bucket named: `adoption-service-testing`. The bucket configurations can be:
        
        Storage class: Coldline
        Location: us-west-1
        Lifecycle: Create a rule to delete all objects older than 1 day to help you keep the bucket clean.
        
- Create a [Service Account](https://console.cloud.google.com/iam-admin/serviceaccounts) with the `Storage Object Admin` role. Check the box that says `Furnish a new private key`. When saving, a json file with the service account private key will download automatically.

- Copy the private key into a file inside the root directory of the application (adoption-service/) with the name `gcloud-service-key.json`:

        Example: cp ~/Downloads/{project-id-key-id.json} gcloud-service-key.json
  
- Build the toolset container:

        make build-toolset
 
- Run: `make all-test` to run all tests and verify everything works as expected.

## Important note

If you want to be able to run integration tests from your IDE, follow the next steps:

- _**(Always, before running the integration tests)**_ Verify that the postgres container is running:

        docker ps -a
        
  If it is not, run: `make deploy-postgres`
  
- _**(Only for the first time)**_ Run:
        
        sudo sh -c "echo '127.0.0.1      postgres' >> /etc/hosts"

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
        

Check the **Makefile** file to discover more tasks.
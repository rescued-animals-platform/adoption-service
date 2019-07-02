FROM google/cloud-sdk:slim

RUN apt-get update -y && apt-get install -y openjdk-8-jdk

RUN java -version

RUN apt-get install -y google-cloud-sdk-app-engine-java

RUN apt-get --only-upgrade -y install google-cloud-sdk

ENTRYPOINT ["/bin/bash"]
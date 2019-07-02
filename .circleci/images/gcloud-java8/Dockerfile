FROM google/cloud-sdk:slim

RUN apt-get update -y && apt-get install -y openjdk-8-jdk

RUN java -version

ENTRYPOINT ["/bin/bash"]
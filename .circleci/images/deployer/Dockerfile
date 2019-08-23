FROM google/cloud-sdk:slim

RUN apt-get install -y kubectl unzip wget

RUN apt-get --only-upgrade -y install google-cloud-sdk

RUN wget https://releases.hashicorp.com/terraform/0.12.7/terraform_0.12.7_linux_amd64.zip

RUN unzip terraform_0.12.7_linux_amd64.zip

RUN mv terraform /usr/local/bin/

RUN terraform --version

ENTRYPOINT ["/bin/bash"]
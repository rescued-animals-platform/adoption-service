FROM google/cloud-sdk:slim

RUN apt-get install -y kubectl

RUN apt-get --only-upgrade -y install google-cloud-sdk

ENTRYPOINT ["/bin/bash"]
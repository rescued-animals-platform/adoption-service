#!/usr/bin/env bash
set -xeuo pipefail

apt-get update;
apt-get install -y unzip wget;
wget https://releases.hashicorp.com/terraform/0.11.7/terraform_0.11.7_linux_amd64.zip;

mkdir -p ~/opt/terraform;
unzip terraform_0.11.1_linux_amd64.zip -d ~/opt/terraform;
cp opt/terraform/terraform /usr/local/bin/;

terraform --version
# Overview
This playbook will execute and deploy parsers and parser resources into an existing Metron system
Please see the roles documention for changes that need to be made

The scripts/deploy_parsers_to_vagrant.sh script can be used to run this playbook against an existing vagrant
install. Note that it uses the existing inventory in this project unless you overide that

## Prerequisites
The following tools are required to run these scripts:

- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)
- [Ansible](http://www.ansible.com/) (version 2.0 or greater)


### Parsers
The playbook will gather the necessary cluster settings from Ambari and install the parsers built in the project and configured in the role files [see roles/README.md].  By default, everything will be configured based on the initial values given to the archetype

### Running the playbook against an existing Metron Vagrant deployment

Navigate to `/metron-deployment/playbooks` and run:
`../scripts/deploy_parsers_to_vagrant.sh -p pathToInventory -v pathToVagrantDir`

     -p --inventory_path: pathToInventory
                         This is the path to the inventory directory to be used
                         This is optional, the default is to the inventory in this project
                         which *should* match any vagrant deployment ( full or quick dev )
     -v  --vagrant_path: pathToVagrantDir
                         This is the path to the vagrant directory used to run the environment
                         from within the metron project
                         incubator-metron/metron-deployment/vagrant/full-dev-platform  for example
                         This is used to find the key to use
                         This is a required parameter

### Running the playbook against an existing Metron deployment

Navigate to `/metron-deployment/playbooks` and run:
`../scripts/deploy_parsers_to_cluster.sh -p pathToInventory`

     -p --inventory_path: pathToInventory
                         This is the path to the inventory directory to be used
                         This is required and should be the inventory used to deploy the cluster
                         All ssh keys etc should be existing and setup

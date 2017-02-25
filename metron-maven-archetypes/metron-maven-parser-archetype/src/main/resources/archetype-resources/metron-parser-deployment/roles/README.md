# Ansible Roles

These are the roles required by ansible to install one or more parsers contained in a
project into an already existing and configured Metron system.

The archetype creation of the project is given the parser name and parser class name, these
are used in the project, and it will work out of the box without need to modify anything, unless
you add another parser or change the parser name.

Listed here are the changes you need to make to deploy your parsers

- ambari_gather_facts
- epel
- httplib2
- metron_elastic_search_templates
    - You must create or modify the sample elasticsearch template for each parser to be deployed
    This is located in files/es_templates
- metron_kafka_topics
    - You must create a topic entry in defaults/main.yml for each of your parsers
- metron_streaming
    - In order to configure for your specific parsers, you need to modify the metron_streaming/defaults/main.yml
    telemetry: ["sample"] should be changed to be the name or names as common separated strings of
    the parsers in this project you want to install
    - You must also modify or create entries here for hdfs_YOURPARSER_purge_cronjob
    - You must create purge settings for each of your parsers in tasks/es_purge.yml and tasks hdfs_purge.yml
- monit
    - You must create an entry for each of your parsers in templates/monit/parsers.monit
- monit-start
- python-pip

# Metron Maven Parser Archetype

This archetype can be used to create a project to create, build and deploy one or more metron parsers
By filling out the parameters when creating the archetype, the produced project will be completely setup
to build and deploy.

## Steps Required

### Build Metron
`cd incubator-metron`
`mvn clean install`

This will build and install the archetype locally 
    TODO: deploy archetype to maven repo

### Create your project 
Create a directory whereever you would like for your project
`mkdir myParser && cd myParser`

### Create the project with maven
`mvn archetype:generate -DarchetypeCatalog=local`

Select the metron-maven-parser-archtype

##### Fill out the parameters
    NOTE: Currently, the version number MUST MATCH THE VERSION OF METRON ( 0.3.1 )
    When prompted for a version you must enter this or your parser will fail to start in storm
    
    NOTE: the parserName must be lowercase, the parserClassName must start be Capital case, the name should match
    for consistancy's sake, but do not have to.  the parserName will be the sensor name used in the system ( the ES index etc )
    whereas the parserClassName will be used for java class names in generated sources

### Examine the generated project, code your parser and tests and then build
`mvn install`

### Then, you can deploy into a running vagrant instance ( full_dev or quick_dev) by running the script with example:
`cd metron-parser-deployment/playbooks`
`bash ../scripts/deploy_parsers_to_vagrant.sh -v /Users/cwalken/src/apache/forks/incubator-metron/metron-deployment/vagrant/full-dev-platform -p ../inventory/full-dev-platform parser_install.yml

See the README.md for metron-parser-deployment/scripts for more information on this script

When this completes your parser will be installed and deployed in metron, and ready for other testing

    ISSUES: 
    
    - the parser will not be monitored or started by MONIT automatically after deployment currently
    you will have to turn on monitoring and start the parser manually
    
    - the deployment restarts all monit services - so it will take a moment to comeback
    
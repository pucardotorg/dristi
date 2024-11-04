# Advocate service

This service handles the registration of new advocates and advocate clerks in the system. Advocates and clerks are created in the user and individual registries with linkages.

## Prerequisites

- Java 17 JDK or JVM installed locally
- Maven 
- Java Spring Boot 3.2.2
- Kafka 3.6+ running locally
- PostgreSQL 14+ locally installed & running or deployed in the cloud

## Dependencies
Below are the DIGIT services that this service depends on:
- MDMS v2.0
- Workflow
- IDGen
- Notification (SMS)
- Individual
- User

## Setup
1. Clone the repository
2. Clone the configs repository for the persister configuration file
3. Import the egov-persister code from DIGIT-Core latest release branch. In the application.properties of the egov-persister, modify the egov.persist.yml.repo.path property to point to the advocate persister YAML file in the configs/egov-persister folder. 
4. Create a database in PostgreSQL
5. Import the project into an IDE and build using Maven
6. Move to the configuration section

## Configuration
1. In application.properties, tweak the database configuration and Flyway configuration sections to reflect the DB name, username and password.
2. Enter the dependent service host URLs:
- egov.individual.host
- egov.mdms.host
- egov.user.host
- egov.idgen.host
- egov.workflow.host

## Running the service
Once above steps are completed, run the persister and the advocate services. 

## API specifications

## Workflows related to advocate module

1. [advocateregistration-workflowConfig.json](../../docs/Advocate/worfkow/advocateregistration-workflowConfig.json)
2. [advocateclerkregistration-workflowConfig - Copy.json](..%2F..%2Fdocs%2FAdvocate%2Fworfkow%2Fadvocateclerkregistration-workflowConfig%20-%20Copy.json)

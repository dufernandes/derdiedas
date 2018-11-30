# Der Die Das
**Software for helping memorizing the German articles along with their respetive words**

## Requirements

In order to handle this project appropriately, please setup the following tools:

 - Git
 - Java (JDK) 8 or higher
 - Maven 3
 - IntelliJ was used for development, but Eclipse should work

## Cloning the project

In order to clone the project, please execute either one of the following commands:

for cloning with SSH
```
git clone git@github.com:dufernandes/derdiedas.git
```

or for cloning with HTTPS

```
https://github.com/dufernandes/derdiedas.git
```

If you have problems cloning the project, you can access it by clicking [here](https://github.com/dufernandes/derdiedas).

## How to test and start the application via command line

Please run all commands below from the root of the project.

In order to run unit and integration tests, execute the following command:

```
mvn verify
```

Should you wish only to run the unit tests, execute the following command:

```
mvn verify -Dit.skip=true
```

For one to start the application, run the following comman:

```
mvn spring-boot:run
```

## Test coverage

After running  `mvn verify` one can access the coverate report at the following file: `/derdiedas/target/site/jacoco/index.html`.

## Database

In order to make things simpler, here only embedded H2 databawse is used. Also, the application is always run and tested (integration tests) using the embedded tomcat. Should one decide to change for a external, more robust database, and to an external Tomcat, in a server, for instance, one should only setup more configuration.

## Accessing local H2 database

After starting the application, one can access the embedded H2 database via the following URL: [http://localhost:8080/h2-console/](http://localhost:8080/h2-console/). Here is the configuration needed:
 - Setting Name: Generic H2 (Embedded)
 - Driver Cass: org.h2.Driver
 - JDBC URL: jdbc:h2:mem:testdb - pay attention becaue by default this is another URL
 - User Name: sa
 - Password: leave it empty
 
## Generating and accessing Javadoc

In order to generate the Javadoc, please run the following command:

```
mvn javadoc:javadoc
```

One can access it in the folder: `derdiedas/target/site/apidocs`.

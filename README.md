# Der Die Das REST API

This is a software for helping memorizing the German articles along with their respective words.

This platform provides the backend API to accomplish the described goal.

## Requirements

In order to handle this project appropriately, please setup the following tools:

 - Git
 - Java (JDK) 8 or higher
 - Maven 3
 - IntelliJ was used for development, but Eclipse should work
 - Curl (only for running examples)

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
mvn clean verify package
```

Should you wish only to run the unit tests, execute the following command:

```
mvn verify -Dit.skip=true
```

For one to start the application, run the following comman:

```
mvn spring-boot:run
```

## Running the application with local database

Here is is shown how to run the application using a local database. Postgres is configured by default, however changing the database is simple enough. Please note that all queries are created using JPQL, thus one can use all JPA supported SQL Databases.

1. Setup your local Postgres. [Here](https://help.ubuntu.com/community/PostgreSQL) is a nice tutorial for Ubuntu. Install the server and the client (PgAdmin).
2. Create a user for accessing databases. Please check the config file (`resources/application-localdb.properties`) to set it up.
2. Create the database. It is used one called `derdiedas`, which of course can be easily changed.
3. The the application using the `localdb` profile, as follows: `mvn spring-boot:run -Dspring-boot.run.profiles=localdb`

For now, it is configured for all tables to be created. Also, the database is populated via the script in `com.derdiedas.bootstrap.DataLoader`.

### Special profile for cockroachdb

As an experiment, there is a profile setup to run the application using [cockroachdb](https://www.cockroachlabs.com/). In order to use it, run the application as:

```
mvn spring-boot:run -Dspring-boot.run.profiles=localcockroachdb
```

Note that this is only a manner to show that this application can be easily run in different databases. In order to set it up properly, it is highly recommended to go through [this tutorial](https://www.baeldung.com/cockroachdb-java).

## Running an example

Next, one can see on how to run a small example authenticating an fetching users using `curl`.

In order to create a user, run the following command:

```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "email": "email@email.com",
    "password": "password",
    "firstName": "first name",
    "lastName": "last name"
}' http://localhost:8080/users
```

Here a user with email "email@emaiol.com" is created. This is the login for the user.

In order to authenticate the user, run the following command:

```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "username": "email@email.com",
    "password": "password"
}' http://localhost:8080/login
```

Here is an example of the HTTP Response from the last command:

```
HTTP/1.1 200 
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJleHAiOjE1NTE5MDAxMjJ9.hVuoNXh1VUq_w4zWZnyzjbg-LXIn4f5Z9o_x6rnW5Z7LHTseGfBi65ichqvyio8693-YOY5ZDn0IG2TNx1fRPg
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Sun, 24 Feb 2019 19:22:02 GMT
```

Please note the `Authorization` part in the HTTP Header. One must copy its content into the following command, which will list a user data with email "email@email.com".

```
curl -i -H "Content-Type: application/json" \
-H "Authorization: Bearer xxx.yyy.zzz" \
-X GET "http://localhost:8080/users?fetchType=email&idOrEmail=email@email.com"
```

After running the command above, note that the body of the HTTP Response will contain the user data, except the password.

For accessing the full API documentation, please go to the [Generating and accessing REST API documentation](#Generating-and-accessing-rest-api-documentation) section.

## Test coverage

After running  `mvn clean verify package` one can access the coverate report at the following file: `/derdiedas/target/site/jacoco/index.html`.

## Database

In order to make things simpler, by default embedded H2 database is used while running the software locally. Also, the application is always run and tested (integration tests) using the embedded tomcat. Should one decide to change for a external, more robust database, and to an external Tomcat, in a server, for instance, one should only change the setup for that.

## Accessing local H2 database

In order to access the H2 database, one must access the application without security, for that run the following command:

```
mvn spring-boot:run -Dspring-boot.run.profiles=nosecure
```

Note that the profile `nosecure` is being used. Basically it disables Spring Security, so H2 can be accessed.

After starting the application, one can access the embedded H2 database via the following URL: [http://localhost:8080/h2-console/](http://localhost:8080/h2-console/). Here is the configuration needed:
 - Setting Name: Generic H2 (Embedded)
 - Driver Cass: org.h2.Driver
 - JDBC URL: jdbc:h2:mem:testdb - pay attention becaue by default this is another URL
 - User Name: sa
 - Password: leave it empty
 
## Documentation
 
### Generating and accessing Javadoc

In order to generate the Javadoc, please run the following command:

```
mvn javadoc:javadoc
```

One can access it in the folder: `derdiedas/target/site/apidocs`.

Alternatively, one can access it after running the application at the following URL: http;//APP_DOMAIN/docs/apidocs/index.html. For instance, running in the local environment, the URL would be: [http://localhost:8080/docs/apidocs/index.html](http://localhost:8080/docs/apidocs/index.html)

Please note that the documentation will be available only after running `mvn clean javadoc:javadoc package`, and later `mvn spring-boot:run`.

## Dockernizing the application

One may also run the application in a docker container. Note tha a `Dockerfile` already exists. Building and running a image, assumes one is using a local H2 database, as it is done for development.

In order to create an image, run the following command (one needs docker installed and configured locally):

```
docker build --tag derdiedas .
```

In order to run the image in port 80, simply run:

```
docker container run -p 80:8080 derdiedas
```

Now, one can access http://localhost and check that the application is up and running.

### Generating and accessing REST API documentation

API documentation is provided by example. For that, while running integration tests, the documentation is created based on these results. Thus, after running `mvn clean verify package` on can find the REST API sample documentation at `derdiedas/target/generated-docs/index.html`. Alternatively, one may access this documentation, after starting the software at the following URL: http;//APP_DOMAIN/docs/api/index.html. For instance, running in the local environment, the URL would be: [http://localhost:8080/docs/api/index.html](http://localhost:8080/docs/api/index.html). 

Please note that the documentation will be available only after running `mvn clean verify package`, and later `mvn spring-boot:run`.

## References

Here one may find many references which were quite useful for creating this project.

### References for authentication

 - Implementing JWT Authentication on Spring Boot APIs - https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 - Allow H2-Console with Spring Authentication - https://www.logicbig.com/tutorials/spring-framework/spring-boot/jdbc-security-with-h2-console.html
 - Spring Security for Spring Boot Integration Tests - https://www.baeldung.com/spring-security-integration-tests
 - How to disable 'X-Frame-Options' response header in Spring Security? - https://stackoverflow.com/questions/28647136/how-to-disable-x-frame-options-response-header-in-spring-security
 
### References for Spring Rest Documentation

 - Spring REST Docs - Test driven documentation of your REST API - https://www.vojtechruzicka.com/spring-rest-docs/
 - Generating documentation for your REST API with Spring REST Docs - https://g00glen00b.be/spring-rest-docs/
 - Introduction to Spring REST Docs - https://www.baeldung.com/spring-rest-docs
 - Creating API Documentation with Restdocs - https://spring.io/guides/gs/testing-restdocs/
 - Making REST Docs available on the created jar - _stackoverflow answer_ - https://stackoverflow.com/questions/49558157/publish-spring-restdocs-html-documentation-with-application
 - Making REST Docs available on the created jar - https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started-build-configuration-packaging-the-documentation

### References for Lombok

 - Jacoco: exclude generated methods of Lombok - https://stackoverflow.com/questions/29520912/jacoco-exclude-generated-methods-using-it-with-lombok
 - Sample project which excludes Lombok code from code coverage - https://github.com/rainerhahnekamp/jacocolombok
 - Sample pom.xml using delombok with a configured target directory - https://github.com/eugenp/tutorials/blob/master/lombok/pom.xml
 
 ### References for Javadoc
 
 - Using Javadoc Resources - maven plugin - https://maven.apache.org/plugins/maven-javadoc-plugin/examples/javadoc-resources.html
 - Configure source directory for javadoc in order to use delombok - https://stackoverflow.com/questions/29563593/lombok-maven-javadocaggregate-report-with-generated-sources
 
 ### References for REST API
 
 - REST Resource Naming Guide - https://restfulapi.net/resource-naming/
 - Restful API design: methods - https://restful-api-design.readthedocs.io/en/latest/methods.html 
 - PUT versus POST - https://stackoverflow.com/questions/630453/put-vs-post-in-rest
 
 ### References to Postgres
 
 - Install Postgres on Ubuntu - https://help.ubuntu.com/community/PostgreSQL
 
 ### References to CockroachDB
 
 - Guide to CockroachDB in Java - https://www.baeldung.com/cockroachdb-java
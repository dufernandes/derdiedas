# Der Die Das REST API

This is a software for helping memorizing the German articles along with their respective words. This platform provides the backend API to accomplish the described goal.

The idea is simple:
1. you register a user - _POST /users_;
2. you authenticate the user - _POST /login_
3. you assign a set of words to this user _PUT /users/{userId}?action=assignLearningWords_;
4. when the user learns a word, save this status - _PUT /learningWords/{learningWordId}?isStudied=true_;
5. when all words from the set is learned, assign a new set to the user - _PUT /users/{userId}?action=assignLearningWords_;
6. when the user has learned all words, the user is with her learning.

For more API details, please have a look at the [Generating and accessing REST API documentation](#Generating-and-accessing-rest-api-documentation) section.

For a better understanding of the architectural decisions of this project, please check its [wiki](https://github.com/dufernandes/derdiedas/wiki).

## Requirements

In order to handle this project appropriately, please setup the following tools:

 - Git
 - Java (JDK) 8 or higher
 - Maven 3
 - IntelliJ was used for development, but Eclipse should work
 - Curl (only for running examples)

## Quick start

Here is how one can quickly use and understand this application. All instructions will be given assuming a local environment.

Firstly, clone the project in your local environment, either by SSH issing this command `git clone git@github.com:dufernandes/derdiedas.git
` or by HTTPS executing this one: `https://github.com/dufernandes/derdiedas.git`.

In the root of the cloned project, open a terminal and run the command below in order to compile, run all tests and generate the documentation:

 ```
 mvn clean verify package
 ```

Now start the application on port `8080`:

 ```
 mvn spring-boot:run
 ```

After that, the application is accessed via the URL http://localhost:8080. Note that the a in-memory H2 database is used.

The next step is to follow the procedures described in the beginning of this documentation. Thus, first a user is created by using the following command, in a separate terminal window: 

```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "email": "email@email.com",
    "password": "password",
    "firstName": "first name",
    "lastName": "last name"
}' http://localhost:8080/users
```

A user with email "email@emaiol.com" is created. This is the login for the user. Next, authenticate the user with the following command:

```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "username": "email@email.com",
    "password": "password"
}' http://localhost:8080/login
```

Here is an example of the HTTP Response from the last command:

```
HTTP/1.1 200 
Authorization: Bearer xxx.yyy.zzz
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
userId: 4437
X-Frame-Options: DENY
Content-Length: 0
Date: Sun, 24 Feb 2019 19:22:02 GMT
```

Please note the `Authorization` part in the HTTP Header. One must copy its content into the following command, which will assign words to the user with email `email@email.com` and id `4437` (taken also from the last response's header value of key `userId`).

```
curl 'http://localhost:8080/users/4437?action=assignLearningWords' -i -X PUT \
    -H 'Content-Type: application/json' \
    -H 'Authorization: Bearer xxx.yyy.zzz'
```

Here is the response holding the words assign for the user to learn:

```
HTTP/1.1 200 OK
Pragma: no-cache
X-XSS-Protection: 1; mode=block
Content-Length: 1824
Expires: 0
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
Content-Type: application/json;charset=UTF-8
Cache-Control: no-cache, no-store, max-age=0, must-revalidate

{
  "id" : 4437,
  "email" : "emailAA@email.com0",
  "firstName" : "firstAA name0",
  "lastName" : "lastAA name0",
  "wordsPerGroup" : 10,
  "studyGroupPage" : 0,
  "wordsStudying" : [ {
    "id" : 4439,
    "word" : {
      "id" : 2655,
      "article" : "das",
      "word" : "Zimmer",
      "translation" : "room"
    },
    "studied" : false
  }, {
    "id" : 4445,
    "word" : {
      "id" : 2650,
      "article" : "der",
      "word" : "Weg",
      "translation" : "way"
    },
    "studied" : false
  }, {
    "id" : 4443,
    "word" : {
      "id" : 2649,
      "article" : "der",
      "word" : "Tag",
      "translation" : "day"
    },
    "studied" : false
  }, {
    "id" : 4444,
    "word" : {
      "id" : 2651,
      "article" : "das",
      "word" : "Auge",
      "translation" : "eye"
    },
    "studied" : false
  }, {
    "id" : 4447,
    "word" : {
      "id" : 2653,
      "article" : "der",
      "word" : "Kopf",
      "translation" : "head"
    },
    "studied" : false
  }, {
    "id" : 4441,
    "word" : {
      "id" : 2648,
      "article" : "die",
      "word" : "Hand",
      "translation" : "hand"
    },
    "studied" : false
  }, {
    "id" : 4442,
    "word" : {
      "id" : 2646,
      "article" : "die",
      "word" : "Zeit",
      "translation" : "time"
    },
    "studied" : false
  }, {
    "id" : 4438,
    "word" : {
      "id" : 2647,
      "article" : "der",
      "word" : "Mann",
      "translation" : "man"
    },
    "studied" : false
  }, {
    "id" : 4440,
    "word" : {
      "id" : 2652,
      "article" : "das",
      "word" : "Ding",
      "translation" : "thing"
    },
    "studied" : false
  }, {
    "id" : 4446,
    "word" : {
      "id" : 2654,
      "article" : "das",
      "word" : "Jahr",
      "translation" : "year"
    },
    "studied" : false
  } ]
}
```
With the words, in hands, whenever the user has lerned one she can set it as studied using the following command:

```
curl 'http://localhost:8080/learningWords/4439?isStudied=true' -i -X PUT \
    -H 'Content-Type: application/json' \
    -H 'Authorization: Bearer xxx.yyy.zzz'
```

Note that the word id `4439` was used, representing the `das Zimmer`. 

Now, one can learn more words, and when she is finished with this group, she can pick new ones assigning words again.

Observe that in all services the `Authorization` was sent in order to make sure the logged in user accesses the services.

In order to access the full API documentation, please go to the [Generating and accessing REST API documentation](#Generating-and-accessing-rest-api-documentation) section.

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

Here is shown how to run the application using a local database. Postgres is configured by default, however changing the database is simple enough. Please note that all queries are created using JPQL, thus one can use all JPA supported SQL Databases.

1. Setup your local Postgres. [Here](https://help.ubuntu.com/community/PostgreSQL) is a nice tutorial for Ubuntu. Install the server and the client (PgAdmin).
2. Create a user for accessing databases. Please check the config file (`resources/application-localdb.properties`) to set it up.
2. Create the database. It is used one called `derdiedas`, which of course can be easily changed.
3. The application using the `localdb` profile, as follows: `mvn spring-boot:run -Dspring-boot.run.profiles=localdb`

For now, it is configured for all tables to be created. Also, the database is populated via the script in `com.derdiedas.bootstrap.DataLoader`.

### Special profile for cockroachdb

As an experiment, there is a profile setup to run the application using [cockroachdb](https://www.cockroachlabs.com/). In order to use it, run the application as:

```
mvn spring-boot:run -Dspring-boot.run.profiles=localcockroachdb
```

Note that this is only a manner to show that this application can be easily run in different databases. In order to set it up properly, it is highly recommended to go through [this tutorial](https://www.baeldung.com/cockroachdb-java).

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

### Generating and accessing REST API documentation

API documentation is provided by example. For that, while running integration tests, the documentation is created based on these results. Thus, after running `mvn clean verify package` on can find the REST API sample documentation at `derdiedas/target/generated-docs/index.html`. Alternatively, one may access this documentation, after starting the software at the following URL: http;//APP_DOMAIN/docs/api/index.html. For instance, running in the local environment, the URL would be: [http://localhost:8080/docs/api/index.html](http://localhost:8080/docs/api/index.html). 

Please note that the documentation will be available only after running `mvn clean verify package`, and later `mvn spring-boot:run`.
 
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
 
 ### References for Spring integration tests
 
 - Testing the Web Layer - https://spring.io/guides/gs/testing-web/
 
 ### References to Postgres
 
 - Install Postgres on Ubuntu - https://help.ubuntu.com/community/PostgreSQL
 
 ### References to CockroachDB
 
 - Guide to CockroachDB in Java - https://www.baeldung.com/cockroachdb-java
 
 ## Author
 
 - Eduardo Antonio Cecilio Fernandes
 - e-mail: eduardo.cecilio@gmail.com
 
 ## License
 
 - [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
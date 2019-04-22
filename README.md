# Der Die Das
**Software for helping memorizing the German articles along with their respetive words**

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
-X GET http://localhost:8080/users?email=email@email.com
```

After running the command above, note that the body of the HTTP Response will contain the user data, except the password.

## Test coverage

After running  `mvn verify` one can access the coverate report at the following file: `/derdiedas/target/site/jacoco/index.html`.

## Database

In order to make things simpler, here only embedded H2 database is used. Also, the application is always run and tested (integration tests) using the embedded tomcat. Should one decide to change for a external, more robust database, and to an external Tomcat, in a server, for instance, one should only setup more configuration.

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

## References

### References for authentication

 - Implementing JWT Authentication on Spring Boot APIs - https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 - Allow H2-Console with Spring Authentication - https://www.logicbig.com/tutorials/spring-framework/spring-boot/jdbc-security-with-h2-console.html
 - Spring Security for Spring Boot Integration Tests - https://www.baeldung.com/spring-security-integration-tests

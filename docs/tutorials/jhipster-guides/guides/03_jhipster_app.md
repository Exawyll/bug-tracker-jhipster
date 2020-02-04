# JHipster App

<walkthrough-tutorial-duration duration="10"></walkthrough-tutorial-duration>

## Running the application

You have now a fully functional application built with JHipster.
This tutorial will show you the features offered by JHipster.

Once the application is generated, you can launch it with the following Maven command:
```bash
./mvnw
```

or on Windows : 
```bash
   mvnw.cmd
```

The application will be accessible locally on the port **8080**. Luckily, Cloud Shell provides a web 
preview functionality that allows you to run web applications on the virtual machine instance.
Click on `walkthrough web-preview-icon` icon and select the port **8080**.
You can check all the common ports available [here](https://www.jhipster.tech/common-ports/).

## File organization
Let's take a look at the project generated by JHipster. Below is a short description of its content.

### Back-end
* The java code is located in src/main/java
* Unit and integration tests are located in src/test/java
* Gatling tests are located in src/test/gatling 

### Front-end
* The front-end is located in src/main/webapp
* The folder app contains the Angular modules
* i18n contains files for internationalization

## Security
To use Spring Security in a Single Web Page Application, like those generated by JHipster, you need Ajax login/logout/error views. JHipster has already configured Spring Security in order to use those views correctly.

By default, JHipster comes with 4 different users:
* "system": for automatic processes, mainly used for our audit logs
* "anonymousUser": given to anonymous users when they do an action 
* "user": a basic user with the authorization "ROLE_USER". His default password is "user"
* "admin": an admin user with the authorizations "ROLE_USER" and "ROLE_ADMIN". His default password is "admin"


The two authorizations “ROLE_USER” and “ROLE_ADMIN” provide the same access to the entities which 
means that a “user” is authorized to perform the same CRUD operations as an “admin”. This behavior can be 
an issue when the application runs in production because a “user” can for example delete any entities.
See more [here](https://www.jhipster.tech/security/).


## Administration dashboard (1/2)
The following parts are available for admin users only, on the menu Administration.

The [JHipster Registry](https://www.jhipster.tech/jhipster-registry/) provides administration dashboards, which are used for all application types.
As soon as an application registers on the Eureka server, it will become available on the dashboards.

### Metrics dashboard
The metrics dashboard uses Micrometer to give a detailed view of the application performance.
It gives metrics on:
* the JVM
* HTTP requests
* cache usage
* database connection pool
 
 By clicking on the Expand button next to the JVM thread metrics you will get a stacktrace of the 
 running application, which is very useful to find out blocked threads.

 ### The health dashboard
 The health dashboard uses Spring Boot Actuator’s health endpoint to give health information on 
 various parts of the application. Many health checks are provided out-of-the-box by Spring Boot 
 Actuator, and it’s also very easy to add application-specific health checks.

## Administration dashboard (2/2)
### The configuration dashboard
The configuration dashboard uses Spring Boot Actuator’s configuration endpoint 
to give a full view of the Spring configuration of the current application.

**The logs dashboard**  

The logs dashboard allows to manage at runtime the Logback configuration of the running application. 
Changing the log level of a Java package is as simple as clicking on a button, which is very convenient in both development and production environments.

---

<walkthrough-conclusion-trophy></walkthrough-conclusion-trophy>

Congratulations!

Enter the next command line to start the next tutorial:

```bash
cloudshell launch-tutorial -d ~/jhipster-guides/guides/04_creating_entities_with_jdl_studio.md;
```
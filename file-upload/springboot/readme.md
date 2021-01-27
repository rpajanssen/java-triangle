# SpringBoot using RestEasy to implement JAX-RS

This module packages the application as a fat jar using [SpringBoot](https://spring.io/projects/spring-boot) and
[RestEasy](https://resteasy.github.io/) as the JAX-RS implementation.

It uses the **resteasy-spring-boot-starter** to integrate SpringBoot with RestEasy.

## Build en run

- build   : ```mvn clean install```
- package : ```mvn clean package```
- run     : ```java -jar target/springboot-<version number>.jar```

You can also run the application using maven:

    mvn spring-boot:run
    
To access the api:

    http://localhost:8080/api/upload
    http://localhost:8080/api/formupload

More demo api calls can be found in the main readme (you may need to switch the application context).





